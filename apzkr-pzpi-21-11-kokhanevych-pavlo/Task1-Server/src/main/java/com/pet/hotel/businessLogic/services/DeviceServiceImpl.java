package com.pet.hotel.businessLogic.services;

import com.pet.hotel.businessLogic.domain.interfaces.DeviceService;
import com.pet.hotel.businessLogic.domain.transferObjects.BlockOfFood;
import com.pet.hotel.businessLogic.domain.transferObjects.RoomDto;
import com.pet.hotel.businessLogic.domain.transferObjects.StateOfRoomRequest;
import com.pet.hotel.businessLogic.domain.transferObjects.StateOfRoomResponse;
import com.pet.hotel.businessLogic.domain.transferObjects.rent.RentDto;
import com.pet.hotel.businessLogic.mappers.Mapper;
import com.pet.hotel.businessLogic.utils.EmailSender;
import com.pet.hotel.data.database.entities.RoomEntity;
import com.pet.hotel.data.database.repositories.RentEntity;
import com.pet.hotel.data.database.repositories.RoomRepository;
import com.pet.hotel.data.database.repositories.UserRepository;
import com.pet.hotel.data.enums.FoodType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.function.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RentEntity rentRepository;

    @Autowired
    private final RestTemplate restTemplate;

    @Autowired
    private Mapper mapper;

    @Autowired
    public DeviceServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static float temperature = 18.0f;

    private static float humidity = 20.0f;

    private static int brightness = 20;

    @Override
    @Async
    public StateOfRoomResponse getStateOfRoom(int roomId) {
        Optional<RoomEntity> roomOptional = roomRepository.findById(roomId);
        if (roomOptional.isPresent()) {
            BlockOfFood blockOfFood = new BlockOfFood(FoodType.WATER, 12.3f, 41);
            BlockOfFood blockOfFood1 = new BlockOfFood(FoodType.SAVORY, 12.3f, 42);
            BlockOfFood blockOfFood2 = new BlockOfFood(FoodType.WATER, 12.3f, 43);
            ArrayList<BlockOfFood> list = new ArrayList<BlockOfFood>();
            list.add(blockOfFood);
            list.add(blockOfFood1);
            list.add(blockOfFood2);
            return ResponseEntity.ok(new StateOfRoomResponse(temperature, humidity, brightness, list)).getBody();
        }
        return null;
    }

    private StateOfRoomRequest getResponseForStateRoom(String arduinoIpAddress) {
        try {
            String url = "http://" + arduinoIpAddress + "/device/3/state";
            return restTemplate.getForObject(url, StateOfRoomRequest.class);
        } catch (RestClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @Async
    public boolean setNewStateOfRoom(int roomId, StateOfRoomResponse stateOfRoomResponse) {
        Optional<RoomEntity> roomOptional = roomRepository.findById(roomId);
        if (roomOptional.isPresent()) {
            temperature = stateOfRoomResponse.getTemperature();
            humidity = stateOfRoomResponse.getHumidity();
            brightness = stateOfRoomResponse.getBrightness();
        }
        return false;
    }

    private boolean sendDeviceNewState(String arduinoIpAddress, StateOfRoomResponse stateOfRoomResponse) {
        try {
            String url = "http://" + arduinoIpAddress + "/setState";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<StateOfRoomResponse> requestEntity = new HttpEntity<>(stateOfRoomResponse, headers);
            restTemplate.postForObject(url, requestEntity, Void.class);
            return true;
        } catch (RestClientException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Async
    public boolean updateStateOfRoom(StateOfRoomRequest stateOfRoomRequest, int roomId) {
        Optional<RoomEntity> roomOptional = roomRepository.findById(roomId);
        if (roomOptional.isPresent()) {
            temperature = stateOfRoomRequest.getTemperature();
            humidity = stateOfRoomRequest.getHumidity();
            brightness = stateOfRoomRequest.getBrightness();
        }
        return false;
    }

    private boolean sendDeviceUpdatedState(String arduinoIpAddress, StateOfRoomRequest stateOfRoomRequest) {
        try {
            String url = "http://" + arduinoIpAddress + "/updateState";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<StateOfRoomRequest> requestEntity = new HttpEntity<>(stateOfRoomRequest, headers);
            restTemplate.postForObject(url, requestEntity, Void.class);
            return true;
        } catch (RestClientException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Async
    public boolean sendEmailToUser(String message, int userId) {
        if (userRepository.findById(userId).isPresent()) {
            EmailSender emailSender = new EmailSender();
            String email = userRepository.findById(userId).get().getEmail();
            emailSender.sendEmail(email, message);
            return true;
        }

        return false;
    }

    @Override
    @Async
    public RentDto getRentingByDateAndRoom(LocalDateTime dateTime, int roomId) {
        if (roomRepository.existsById(roomId)) {
            return mapper.map(rentRepository.findRentByDateAndRoom(roomId, dateTime), RentDto.class);
        }

        return null;
    }

    @Override
    @Async
    public RoomDto getInfoRoomByIP(String ip) {
        return mapper.map(roomRepository.getRoomByIP(ip), RoomDto.class);
    }

    @Override
    @Async
    public Mono<ServerResponse> startVideoStream(int roomId) {
        Optional<RoomEntity> roomOptional = roomRepository.findById(roomId);
        if (roomOptional.isPresent()) {
            String arduinoIpAddress = roomOptional.get().getIp();
            return startVideoStreamFromDevice(arduinoIpAddress);
        }
        return Mono.just(ServerResponse.badRequest().build());
    }

    private Mono<ServerResponse> startVideoStreamFromDevice(String arduinoIpAddress) {
        String url = "http://" + arduinoIpAddress + "/video-stream";
        Flux<byte[]> videoStream = WebClient.builder()
                .baseUrl(url)
                .build()
                .get()
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .retrieve()
                .bodyToFlux(byte[].class);

        return Mono.just(ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(BodyInserters.fromPublisher(videoStream, byte[].class)));
    }

    @Override
    @Async
    public Mono<ServerResponse> stopVideoStream(int roomId) {
        Optional<RoomEntity> roomOptional = roomRepository.findById(roomId);
        if (roomOptional.isPresent()) {
            String arduinoIpAddress = roomOptional.get().getIp();
            return stopVideoStreamFromDevice(arduinoIpAddress);
        }
        return Mono.just(ServerResponse.badRequest().build());
    }

    private Mono<ServerResponse> stopVideoStreamFromDevice(String arduinoIpAddress) {

        String url = "http://" + arduinoIpAddress + "/stop-video-stream";
        return WebClient.builder()
                .baseUrl(url)
                .build()
                .post()
                .exchangeToMono(clientResponse -> {
                    HttpStatusCode status = clientResponse.statusCode();
                    if (status.is2xxSuccessful()) {
                        return Mono.just(ServerResponse.ok().build());
                    } else {
                        return Mono.just(ServerResponse.status(status).build());
                    }
                });
    }
}
