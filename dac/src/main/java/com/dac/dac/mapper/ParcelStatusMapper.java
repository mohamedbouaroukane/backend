package com.dac.dac.mapper;

import com.dac.dac.constants.ParcelStatusConst;
import com.dac.dac.entity.LocalParcel;
import com.dac.dac.entity.ParcelStatus;
import com.dac.dac.payload.ParcelStatusDto;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring",nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)

public interface ParcelStatusMapper {


    @Mapping(source = "parcelStatus", target = "address", qualifiedByName = "address")
    @Mapping(source = "parcelStatus", target = "status", qualifiedByName = "status")
    @Mapping(source = "parcelStatus", target = "date", qualifiedByName = "date")
    @Mapping(source = "parcelStatus.status.id", target = "statusID" )
    public ParcelStatusDto mapToDto(ParcelStatus parcelStatus);

    public List<ParcelStatusDto> mapToDto(List<ParcelStatus> parcelStatuses);
    @Named("address")
    default String maoToLocationDto(ParcelStatus parcelStatus) {
        String sendingAddress = "";
        if(parcelStatus.getParcel() instanceof LocalParcel localParcel) {
            sendingAddress = localParcel.getSenderLocker().getParcelLocker().getAddress().getStreet();
        }
        return switch (parcelStatus.getStatus().getStatusLabel()) {
            case CREATED -> " ";
            case CTR_HUB -> "CTR Biskra Hub";
            case SENDER_COURIER -> "in the route to CTR Biskra Hub";
            case SENDER_LOCKER -> sendingAddress;
            case RECEIVER_LOCKER -> parcelStatus.getParcel().getReceiverLocker().getParcelLocker().getAddress().toString();
            case RECEIVER_COURIER -> "In the Route to Receiver Parcel Locker";
            case DELIVERED -> "collected by the receiver";
            case OVER_WEIGHT -> "CTR Biskra Hub";
            case PAYDED -> "CTR Biskra Hub";

            default -> "";
        };
    }
    @Named("status")
    default String maoToStatusDto(ParcelStatus parcelStatus) {
        return switch (parcelStatus.getStatus().getStatusLabel()) {
            case CREATED -> "Created by Sender";
            case CTR_HUB -> "Inside CTR Biskra Hub";
            case SENDER_COURIER -> "In the Route to CTR Biskra Hub";
            case SENDER_LOCKER -> "In the Sender Parcel Locker";
            case RECEIVER_COURIER -> "In the Route to Parcel Locker";
            case RECEIVER_LOCKER -> "In the Receiver Parcel Locker";
            case OVER_WEIGHT -> "Over Weight Detected";
            case PAYDED -> "Sender Payed Over Weight Tax";
            case DELIVERED -> "Delivered";
            default -> "";
        };
    }
    @Named("date")
    default String maoToDateDto(ParcelStatus parcelStatus){
        LocalDateTime localDateTime = parcelStatus.getDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE dd/MM/yyyy HH:mm");

        return localDateTime.format(formatter);
    }



}
