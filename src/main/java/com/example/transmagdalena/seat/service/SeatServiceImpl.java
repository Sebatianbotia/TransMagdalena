package com.example.transmagdalena.seat.service;

import com.example.transmagdalena.bus.Bus;
import com.example.transmagdalena.bus.service.BusService;
import com.example.transmagdalena.seat.DTO.SeatDTO;
import com.example.transmagdalena.seat.Seat;
import com.example.transmagdalena.seat.mapper.SeatMapper;
import com.example.transmagdalena.seat.repository.SeatRepository;
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
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final SeatMapper seatMapper;
    private final BusService busService;

    @Override
    @Transactional
    public SeatDTO.seatResponse save(SeatDTO.seatCreateRequest seatDTO) {
        var f = seatMapper.toEntity(seatDTO);
        f.setBus(busService.getObject(seatDTO.busId()));
        if (isSeatNumberFree(seatDTO.number(), f.getBus().getId())
                && seatRepository.countByBusId(f.getBus().getId()) <= f.getBus().getCapacity() ){
            f.setNumber(seatDTO.number());
        }
        f = seatRepository.save(f);
        return seatMapper.toSeatResponse(f);
    }

    @Override
    @Transactional
    public SeatDTO.seatResponse update(SeatDTO.seatUpdateRequest seatUpdateRequest, Long id) {
        var f = getObject(id);
        seatMapper.updateSeat(f, seatUpdateRequest);
        f.setBus(busService.getObject(seatUpdateRequest.busId()));
        if (seatUpdateRequest.number() != null && isSeatNumberFree(seatUpdateRequest.number(), f.getBus().getId())) {
            f.setNumber(seatUpdateRequest.number());
        }
        return seatMapper.toSeatResponse(f);
    }

    @Override
    public void delete(Long id) {seatRepository.deleteById(id);}


    @Override
    @Transactional(readOnly = true)
    public SeatDTO.seatResponse get(Long id) {
        return seatMapper.toSeatResponse(getObject(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SeatDTO.seatResponse> getAll(Pageable pageable) {
        return seatRepository.findAll(pageable).map(seatMapper::toSeatResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Seat getSeatByNumberAndBusId(int number, Long busId){
        return seatRepository.findByNumberAndBusId(number, busId).orElseThrow(()->new NotFoundException("Seat not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Seat getObject(Long id){
        return seatRepository.findById(id).orElseThrow(() -> new NotFoundException("Seat not found"));
    }

    private Boolean isSeatNumberFree(Integer number, Long busId){
        var s = seatRepository.findByNumberAndBusId(number, busId);
        if (s.isEmpty()) {return true;}
        throw new IllegalArgumentException("numero de asiento ocupado");
    }

}
