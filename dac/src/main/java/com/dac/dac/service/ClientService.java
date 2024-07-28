package com.dac.dac.service;

import com.dac.dac.constants.ExceptionMessages;
import com.dac.dac.constants.UserRole;
import com.dac.dac.entity.Client;
import com.dac.dac.exption.DuplicatedEntryException;
import com.dac.dac.exption.EmptyListRecordException;
import com.dac.dac.exption.RecordNotFoundException;
import com.dac.dac.mapper.ClientMapper;
import com.dac.dac.payload.ClientDto;
import com.dac.dac.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ClientService {

    Logger logger = LoggerFactory.getLogger(ClientService.class);

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientMapper clientMapper;


    public List<ClientDto> getAllClients(){
        List<Client> clients = clientRepository.findAll();
        if(clients.isEmpty()){
            throw new EmptyListRecordException(String.format(ExceptionMessages.EMPTY_RECORDS_LIST_EXCEPTION,"Clients"));
        }
        List<ClientDto> clientsDto = clientMapper.mapToDto(clients);
        return clientsDto;
    }
    public ClientDto getClientByID(Integer id) {
        return this.clientMapper.mapToDto(clientRepository.findById(id)
                .orElseThrow(()-> new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"client","id",id))));
    }
    public ClientDto saveClient(ClientDto clientDto){
        Map<String,String> fields = new HashMap<>();
        boolean emailExist = clientRepository.existsByEmail(clientDto.getEmail());
        boolean phoneExist = clientRepository.existsByPhone(clientDto.getPhone());
        logger.info("email exist" + phoneExist);
        if( emailExist && phoneExist){
            fields.put("email","This email already exists");
            fields.put("phone","This phone already exists");
        }else if(emailExist || phoneExist){
            if(emailExist){
                fields.put("email","This email already exists");
            }else{
                fields.put("phone","This phone already exists");
            }
        }

        if(!fields.isEmpty()){
            throw new DuplicatedEntryException(fields);
        }
        Client client = clientMapper.mapToEntity(clientDto);
        client.setUserRole(UserRole.CLIENT);
        return  this.clientMapper.mapToDto(clientRepository.save(client));
    }

    public ClientDto updateClient(ClientDto clientDto) {
        Client client = clientRepository.findById(clientDto.getId())
                        .orElseThrow(()->new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"client","id",clientDto.getId())));
        clientMapper.updateEntity(clientDto,client);
        return clientMapper.mapToDto(clientRepository.save(client));
    }
    public void deleteClient(Integer id){
        if(!clientRepository.existsById(id)){
            throw new RecordNotFoundException(String.format(ExceptionMessages.RECORD_NOT_FOUND_EXCEPTION,"client","id",id));
        }

         clientRepository.deleteById(id);
    }
}
