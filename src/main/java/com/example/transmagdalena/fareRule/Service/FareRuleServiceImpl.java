package com.example.transmagdalena.fareRule.Service;

import com.example.transmagdalena.fareRule.DTO.FareRuleDTO;
import com.example.transmagdalena.fareRule.FareRule;
import com.example.transmagdalena.fareRule.Mapper.FareRuleMapper;
import com.example.transmagdalena.fareRule.repository.FareRuleRepository;
import com.example.transmagdalena.stop.Service.StopService;
import com.example.transmagdalena.utilities.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FareRuleServiceImpl implements  FareRuleService {

    private final FareRuleRepository fareRuleRepository;

    private final FareRuleMapper fareRuleMapper;
    private final StopService stopService;

    @Override
    @Transactional
    public FareRuleDTO.fareRuleResponse save(FareRuleDTO.fareRuleCreateRequest request) {
        var s = fareRuleMapper.toEntity(request);
        if (request.originId() == request.destinationId()){
            throw new IllegalArgumentException("origen debe ser diferente a destino");
        }
        s.setOrigin(stopService.getObject(request.originId()));
        s.setDestination(stopService.getObject(request.destinationId()));
        return fareRuleMapper.toDto(fareRuleRepository.save(s));
    }

    @Override
    @Transactional(readOnly = true)
    public FareRuleDTO.fareRuleResponse get(Long id) {
        return fareRuleMapper.toDto(getObject(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FareRuleDTO.fareRuleResponse> getAll(Pageable pageable) {
        return  fareRuleRepository.findAll(pageable).map(fareRuleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public FareRule getObject(Long id) {
        return fareRuleRepository.findById(id).orElseThrow(() -> new NotFoundException("fareRule not found"));
    }

    @Override
    public boolean delete(Long id) {
        fareRuleRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional
    public FareRuleDTO.fareRuleResponse update(FareRuleDTO.fareRuleUpdateRequest request, Long id) {
        var s =  getObject(id);
        if (request.originId() == request.destinationId()) { // se encarga que se tenga el mismo origin y destino, los nulos los evita el mapper
            throw new IllegalArgumentException("originId and destinationId cannot be the same");
        }
        fareRuleMapper.update(request, s);
        if (request.originId() != null){
            s.setOrigin(stopService.getObject(request.originId()));
        }
        if (request.destinationId() != null){
            s.setDestination(stopService.getObject(request.destinationId()));
        }
        return fareRuleMapper.toDto(s);
    }
}
