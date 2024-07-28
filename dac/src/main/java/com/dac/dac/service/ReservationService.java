package com.dac.dac.service;

import com.dac.dac.constants.ExceptionMessages;
import com.dac.dac.constants.LockerStatus;
import com.dac.dac.entity.Locker;
import com.dac.dac.entity.Reservation;
import com.dac.dac.exption.RecordNotFoundException;
import com.dac.dac.mapper.ReservationMapper;
import com.dac.dac.payload.request.ReservationRequestDto;
import com.dac.dac.payload.response.ReservationResponseDto;
import com.dac.dac.repository.ClientRepository;
import com.dac.dac.repository.LockerRepository;
import com.dac.dac.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private LockerRepository lockerRepository;

    @Autowired
    private GenerateReservationCodeService generateReservationCodeService;

    @Autowired
    private GenerateBarCodeService generateBarCodeService;
    @Transactional
    public Reservation createReservation(ReservationRequestDto reservationRequestDto) {
        LocalDateTime now = LocalDateTime.now();

        if(checkReservationTime(now)){
            return null;
        }
        String code = generateReservationCodeService.generateReservationCode();
        Reservation reservation = reservationMapper.mapToEntity(reservationRequestDto);
        Locker locker = lockerRepository.findFirstByStatusAndLockerSizeIdAndParcelLockerId(LockerStatus.AVAILABLE,reservationRequestDto.getParcelSize(),reservationRequestDto.getSenderParcelLockerId()).orElseThrow(()->new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"locker","reservation",reservationRequestDto.getSenderParcelLockerId())));
        locker.setStatus(LockerStatus.NOT_AVAILABLE);
        reservation.setLocker(locker);
        reservation.setCreatedIn(now);
        reservation.setExpiredIn(now.plusHours(4));

        reservation.setReservationCode(code);
        try {
     //       reservation.setQRCode(StreamUtil.inputStreamToBytes(generateBarCodeService.generateQRCode(code)));
        }catch (Exception e){
            e.printStackTrace();
        }

        return reservationRepository.save(reservation);
    }
    private boolean checkReservationTime(LocalDateTime reservationDate) {
        LocalTime reservationTime = reservationDate.toLocalTime();
        LocalTime startTime = LocalTime.of(6, 0);
        LocalTime endTime = LocalTime.of(22, 0);

        return reservationTime.isBefore(startTime) || reservationTime.isAfter(endTime);
    }
    public boolean deleteReservation(Long reservationId,int clientId) {
//        Client client = clientRepository.findById(clientId).orElseThrow(()->new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"client","id",clientId)));
//        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(()-> new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"reservation","id",reservationId)));
//        if(reservation.getParcel().getSender() == client && !reservation.getIsExpired() && !reservation.getIsUsed()){
//            reservation.getLocker().setStatus(LockerStatus.NOT_AVAILABLE);
//            reservationRepository.delete(reservation);
//            return true;
//        }
        return false;

    }

    public List<ReservationResponseDto> getReservationByClient(int clientId) {
        clientRepository.findById(clientId).orElseThrow(()->new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"client","id",clientId)));
        return reservationMapper.mapToDto(reservationRepository.findAllByParcelSenderId(clientId));
    }
    public List<ReservationResponseDto> getAllReservation() {
        return reservationMapper.mapToDto(reservationRepository.findAllByOrderByCreatedInDesc());
    }
}
