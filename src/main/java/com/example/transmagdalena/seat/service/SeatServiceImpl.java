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

    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private SeatMapper seatMapper;

    @Autowired
    private BusService busService;

    @Override
    public SeatDTO.seatResponse save(SeatDTO.seatCreateRequest seatDTO) {
        var f = seatMapper.toEntity(seatDTO);
        f.addBus(busService.getObject(seatDTO.busId())); //
        f = seatRepository.save(f);
        return seatMapper.toSeatResponse(f);
    }

    @Override
    @Transactional
    public SeatDTO.seatResponse update(SeatDTO.seatUpdateRequest seatUpdateRequest, Long id) {
        var f = getObject(id);
        Bus bus = f.getBus();
        if (f.getBus().getId() != seatUpdateRequest.busId()) {
            f.removeBus(bus);
            bus =  busService.getObject(seatUpdateRequest.busId());
        }
        seatMapper.updateSeat(f, seatUpdateRequest);
        f.addBus(bus);
        return seatMapper.toSeatResponse(f);
    }

    @Override
    public boolean delete(Long id) {
        var f = getObject(id);
        var check = false;
        if (f != null) {
            f.removeBus(f.getBus());
            seatRepository.deleteById(id);
            check = true;
        }
        return check;
    }

    @Override
    public SeatDTO.seatResponse get(Long id) {
        return seatMapper.toSeatResponse(getObject(id));
    }

    @Override
    public Page<SeatDTO.seatResponse> getAll(Pageable pageable) {
        return seatRepository.findAll(pageable).map(seatMapper::toSeatResponse);
    }

    @Override
    public Seat getSeatByNumberAndBusId(int number, Long busId){
        return seatRepository.findByNumberAndBusId(number, busId).orElseThrow(()->new NotFoundException("Seat not found"));
    }

    @Override
    public Seat getObject(Long id){
        return seatRepository.findById(id).orElseThrow(() -> new NotFoundException("Seat not found"));
    }
}
