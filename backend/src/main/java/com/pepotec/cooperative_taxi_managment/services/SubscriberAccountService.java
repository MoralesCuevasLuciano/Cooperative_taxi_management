package com.pepotec.cooperative_taxi_managment.services;

import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.exceptions.ResourceNotFoundException;
import com.pepotec.cooperative_taxi_managment.models.dto.person.subscriber.account.SubscriberAccountDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.person.subscriber.account.SubscriberAccountCreateDTO;
import com.pepotec.cooperative_taxi_managment.models.entities.SubscriberAccountEntity;
import com.pepotec.cooperative_taxi_managment.models.entities.SubscriberEntity;
import com.pepotec.cooperative_taxi_managment.repositories.SubscriberAccountRepository;
import com.pepotec.cooperative_taxi_managment.validators.SubscriberAccountValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriberAccountService {

    @Autowired
    private SubscriberAccountRepository subscriberAccountRepository;

    @Autowired
    @org.springframework.context.annotation.Lazy
    private SubscriberService subscriberService;

    @Autowired
    private SubscriberAccountValidator subscriberAccountValidator;

    public SubscriberAccountDTO createSubscriberAccount(Long subscriberId, SubscriberAccountCreateDTO account) {
        subscriberAccountValidator.validateSubscriberAccountCreateFields(account.getBalance(), account.getLastModified());

        SubscriberEntity subscriber = subscriberService.getSubscriberEntityById(subscriberId);

        subscriberAccountValidator.validateUniqueSubscriberAccount(subscriber.getId());

        SubscriberAccountEntity entity = convertCreateDtoToEntity(account);
        entity.setSubscriber(subscriber);

        if (entity.getLastModified() == null) {
            entity.setLastModified(LocalDate.now());
        }

        return convertToDTO(subscriberAccountRepository.save(entity));
    }

    public SubscriberAccountDTO getSubscriberAccountById(Long id) {
        SubscriberAccountEntity account = subscriberAccountRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(id, "Cuenta de Abonado"));
        return convertToDTO(account);
    }

    public SubscriberAccountDTO getSubscriberAccountBySubscriberId(Long subscriberId) {
        subscriberAccountValidator.validateSubscriberIdNotNull(subscriberId);
        SubscriberAccountEntity account = subscriberAccountRepository.findBySubscriberIdAndActiveTrue(subscriberId)
            .orElseThrow(() -> new ResourceNotFoundException(subscriberId, "Cuenta de Abonado para el abonado"));
        return convertToDTO(account);
    }

    public List<SubscriberAccountDTO> getAllSubscriberAccounts() {
        return subscriberAccountRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<SubscriberAccountDTO> getActiveSubscriberAccounts() {
        return subscriberAccountRepository.findByActiveTrue().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public SubscriberAccountDTO updateSubscriberAccount(SubscriberAccountDTO account) {
        subscriberAccountValidator.validateIdNotNullForUpdate(account.getId());

        SubscriberAccountEntity entity = subscriberAccountRepository.findById(account.getId())
            .orElseThrow(() -> new ResourceNotFoundException(account.getId(), "Cuenta de Abonado"));

        subscriberAccountValidator.validateSubscriberAccountFields(account);

        SubscriberEntity subscriber = subscriberService.getSubscriberEntityById(account.getSubscriberId());

        entity.setSubscriber(subscriber);
        entity.setBalance(account.getBalance());
        entity.setLastModified(account.getLastModified() != null ? account.getLastModified() : LocalDate.now());

        return convertToDTO(subscriberAccountRepository.save(entity));
    }

    public void deleteSubscriberAccount(Long id) {
        subscriberAccountValidator.validateIdNotNull(id);

        SubscriberAccountEntity entity = subscriberAccountRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(id, "Cuenta de Abonado"));

        entity.setActive(false);
        entity.setLastModified(LocalDate.now());
        subscriberAccountRepository.save(entity);
    }

    private SubscriberAccountEntity convertToEntity(SubscriberAccountDTO dto) {
        SubscriberAccountEntity entity = SubscriberAccountEntity.builder()
            .id(dto.getId())
            .balance(dto.getBalance())
            .lastModified(dto.getLastModified())
            .build();
        return entity;
    }

    private SubscriberAccountEntity convertCreateDtoToEntity(SubscriberAccountCreateDTO dto) {
        SubscriberAccountEntity entity = SubscriberAccountEntity.builder()
            .balance(dto.getBalance())
            .lastModified(dto.getLastModified())
            .active(true)
            .build();
        return entity;
    }

    private SubscriberAccountDTO convertToDTO(SubscriberAccountEntity entity) {
        if (entity == null) {
            return null;
        }

        return SubscriberAccountDTO.builder()
            .id(entity.getId())
            .subscriberId(entity.getSubscriber().getId())
            .subscriber(subscriberService.getSubscriberById(entity.getSubscriber().getId()))
            .balance(entity.getBalance())
            .lastModified(entity.getLastModified())
            .active(entity.getActive())
            .build();
    }

    /**
     * Método auxiliar para actualizar la entidad directamente (para uso interno).
     */
    public SubscriberAccountEntity updateAccountEntity(SubscriberAccountEntity entity) {
        return subscriberAccountRepository.save(entity);
    }

    /**
     * Método auxiliar para obtener la entidad por ID (uso interno).
     */
    public SubscriberAccountEntity getSubscriberAccountEntityById(Long id) {
        return subscriberAccountRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(id, "Cuenta de Abonado"));
    }
}


