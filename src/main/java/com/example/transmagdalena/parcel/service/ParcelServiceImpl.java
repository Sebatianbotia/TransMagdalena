package com.example.transmagdalena.parcel.service;

import com.example.transmagdalena.parcel.DTO.ParcelDTO;
import com.example.transmagdalena.parcel.Mapper.ParcelMapper;
import com.example.transmagdalena.parcel.Parcel;
import com.example.transmagdalena.parcel.repository.ParcelRepository;
import com.example.transmagdalena.utilities.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ParcelServiceImpl implements ParcelService {

    @Autowired
    private final ParcelRepository parcelRepository;

    @Autowired
    private final ParcelMapper parcelMapper;

    @Override
    public ParcelDTO.parcelResponse save(ParcelDTO.parcelCreateRequest parcelDTO) {
        var s = parcelMapper.toEntity(parcelDTO);
        s = parcelRepository.save(s);
        return parcelMapper.toDto(s);
    }

    @Override
    public Page<ParcelDTO.parcelResponse> getAll(Pageable pageable) {
        return parcelRepository.findAll(pageable).map(parcelMapper::toDto);
    }

    @Override
    public ParcelDTO.parcelResponse get(Long id) {
        return parcelMapper.toDto(getObject(id));
    }

    @Override
    public ParcelDTO.parcelResponse get(String tagCode) {
        return parcelMapper.toDto(getObject(tagCode));
    }

    @Override
    public boolean delete(Long id) {
        var s = getObject(id);
        var check = false;
        if (s!=null) {
            parcelRepository.deleteById(id);
            check = true;
        }
        return check;
    }

    @Override
    public Parcel getObject(Long id) {
        return parcelRepository.findById(id).orElseThrow(() -> new NotFoundException("Parcel not found"));
    }

    @Override
    public Parcel getObject(String tagCode) {
        return parcelRepository.findByCode(tagCode).orElseThrow(() -> new NotFoundException("Parcel not found"));
    }

    @Override
    public ParcelDTO.parcelResponse update(ParcelDTO.parcelUpdateRequest parcelDTO, Long id) {
        var s =  getObject(id);
        parcelMapper.update(parcelDTO, s);
        return parcelMapper.toDto(s);
    }
}
