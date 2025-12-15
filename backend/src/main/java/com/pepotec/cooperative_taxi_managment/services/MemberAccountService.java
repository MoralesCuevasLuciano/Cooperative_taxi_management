package com.pepotec.cooperative_taxi_managment.services;

import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import com.pepotec.cooperative_taxi_managment.exceptions.ResourceNotFoundException;
import com.pepotec.cooperative_taxi_managment.models.dto.person.member.account.MemberAccountDTO;
import com.pepotec.cooperative_taxi_managment.models.dto.person.member.account.MemberAccountCreateDTO;
import com.pepotec.cooperative_taxi_managment.models.entities.MemberAccountEntity;
import com.pepotec.cooperative_taxi_managment.models.entities.MemberEntity;
import com.pepotec.cooperative_taxi_managment.repositories.MemberAccountRepository;
import com.pepotec.cooperative_taxi_managment.validators.MemberAccountValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberAccountService {

    @Autowired
    private MemberAccountRepository memberAccountRepository;

    @Autowired
    @org.springframework.context.annotation.Lazy
    private MemberService memberService;

    @Autowired
    private MemberAccountValidator memberAccountValidator;

    public MemberAccountDTO createMemberAccount(Long memberId, MemberAccountCreateDTO account) {
        memberAccountValidator.validateMemberAccountCreateFields(account.getBalance(), account.getLastModified());

        MemberEntity member = memberService.getMemberEntityById(memberId);

        memberAccountRepository.findByMemberId(member.getId()).ifPresent(existing -> {
            throw new InvalidDataException("El socio ya tiene una cuenta asociada");
        });

        MemberAccountEntity entity = convertCreateDtoToEntity(account);
        entity.setMember(member);

        if (entity.getLastModified() == null) {
            entity.setLastModified(LocalDate.now());
        }

        return convertToDTO(memberAccountRepository.save(entity));
    }

    public MemberAccountDTO getMemberAccountById(Long id) {
        MemberAccountEntity account = memberAccountRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(id, "Cuenta de Socio"));
        return convertToDTO(account);
    }

    public MemberAccountDTO getMemberAccountByMemberId(Long memberId) {
        if (memberId == null) {
            throw new InvalidDataException("El ID del socio no puede ser nulo");
        }
        MemberAccountEntity account = memberAccountRepository.findByMemberIdAndActiveTrue(memberId)
            .orElseThrow(() -> new ResourceNotFoundException(memberId, "Cuenta de Socio para el socio"));
        return convertToDTO(account);
    }

    public List<MemberAccountDTO> getAllMemberAccounts() {
        return memberAccountRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<MemberAccountDTO> getActiveMemberAccounts() {
        return memberAccountRepository.findByActiveTrue().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public MemberAccountDTO updateMemberAccount(MemberAccountDTO account) {
        if (account.getId() == null) {
            throw new InvalidDataException("El ID de la cuenta no puede ser nulo para actualizar");
        }

        MemberAccountEntity entity = memberAccountRepository.findById(account.getId())
            .orElseThrow(() -> new ResourceNotFoundException(account.getId(), "Cuenta de Socio"));

        memberAccountValidator.validateMemberAccountFields(account);

        MemberEntity member = memberService.getMemberEntityById(account.getMemberId());

        entity.setMember(member);
        entity.setBalance(account.getBalance());
        entity.setLastModified(account.getLastModified() != null ? account.getLastModified() : LocalDate.now());

        return convertToDTO(memberAccountRepository.save(entity));
    }

    public void deleteMemberAccount(Long id) {
        if (id == null) {
            throw new InvalidDataException("El ID de la cuenta no puede ser nulo");
        }

        MemberAccountEntity entity = memberAccountRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(id, "Cuenta de Socio"));

        entity.setActive(false);
        entity.setLastModified(LocalDate.now());
        memberAccountRepository.save(entity);
    }

    private MemberAccountEntity convertToEntity(MemberAccountDTO dto) {
        MemberAccountEntity entity = MemberAccountEntity.builder()
            .id(dto.getId())
            .balance(dto.getBalance())
            .lastModified(dto.getLastModified())
            .build();
        return entity;
    }

    private MemberAccountEntity convertCreateDtoToEntity(MemberAccountCreateDTO dto) {
        MemberAccountEntity entity = MemberAccountEntity.builder()
            .balance(dto.getBalance())
            .lastModified(dto.getLastModified())
            .build();
        return entity;
    }

    private MemberAccountDTO convertToDTO(MemberAccountEntity entity) {
        if (entity == null) {
            return null;
        }

        return MemberAccountDTO.builder()
            .id(entity.getId())
            .memberId(entity.getMember().getId())
            .member(memberService.getMemberById(entity.getMember().getId()))
            .balance(entity.getBalance())
            .lastModified(entity.getLastModified())
            .active(entity.getActive())
            .build();
    }
}


