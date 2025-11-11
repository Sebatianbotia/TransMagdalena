package com.example.transmagdalena.ticket.service;

import com.example.transmagdalena.route.service.RouteService;
import com.example.transmagdalena.routeStop.RouteStop;
import com.example.transmagdalena.seatHold.service.SeatHoldService;
import com.example.transmagdalena.stop.Service.StopService;
import com.example.transmagdalena.ticket.DTO.TicketDTO;
import com.example.transmagdalena.ticket.Mapper.TicketMapper;
import com.example.transmagdalena.ticket.Ticket;
import com.example.transmagdalena.ticket.repository.TicketRepository;
import com.example.transmagdalena.trip.repository.TripRepository;
import com.example.transmagdalena.trip.service.TripService;
import com.example.transmagdalena.user.Service.UserService;
import com.example.transmagdalena.utilities.QR.QRCodeGenerator;
import com.example.transmagdalena.utilities.clodinary.CloudinaryStorageService;
import com.example.transmagdalena.utilities.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final TripService tripService;
    private final StopService stopService;
    private final UserService userService;
    private final QRCodeGenerator generator;
    private final CloudinaryStorageService storageService;

    @Override
    public TicketDTO.ticketResponse create(TicketDTO.ticketCreateRequest request) {
        if (request.price().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }
        var ticket = ticketMapper.toEntity(request);
        var origin = stopService.getObject(request.originId());
        var destination = stopService.getObject(request.destinationId());
        var user = userService.getObject(request.userId());
        var trip = tripService.getObject(request.tripId());

        //agregar QR aquí
        String ticketCode = generateTicketCode();
        ticket.setTicketCode(ticketCode);

        ticket.addOrigin(origin);
        ticket.addDestination(destination);
        ticket.addUser(user);
        ticket.addTrip(trip);
        var savedTicket = ticketRepository.save(ticket);
        // generar y subir QR
        // por que despues de guardar? porque necesitamos el id del ticket
        try {
            // crear contenido del QR (incluye id y codigo)
            String qrContent = generator.generateTicketQRContent(
                    savedTicket.getId(),
                    ticketCode
            );

            //  generar imagen del QR como bytes
            byte[] qrCodeBytes = generator.generateQRCodeImage(qrContent);

            // subir a Cloudinary y obtener url
            String qrUrl = storageService.uploadQRCode(qrCodeBytes, ticketCode);

            // guardar URL en el ticket
            savedTicket.setQrCodeUrl(qrUrl);
            savedTicket = ticketRepository.save(savedTicket);

        } catch (Exception e) {
            // Si falla el QR el ticket ya esta creado
            throw new RuntimeException("Error generando código QR para el ticket", e);
        }

        return ticketMapper.toDto(savedTicket);

    }

    @Override
    public TicketDTO.ticketResponse update(Long id, TicketDTO.ticketUpdateRequest request) {
        var ticket = getObject(id);
        ticketMapper.update(request,ticket);
        if(request.destinationId() != null) {
            ticket.addDestination(stopService.getObject(request.destinationId()));
        }
        if(request.originId() != null) {
            ticket.addOrigin(stopService.getObject(request.originId()));
        }
        if(request.userId() != null) {
            ticket.addUser(userService.getObject(request.userId()));
        }
        if(request.tripId() != null) {
            ticket.addTrip(tripService.getObject(request.tripId()));
        }
        //Hacer el update al qrUrl
        return ticketMapper.toDto(ticketRepository.save(ticket));
    }

    @Override
    public void delete(Long id) {
        var ticket = getObject(id);
        if(ticket.getQrCodeUrl()!=null){
            try{
                storageService.deleteQRCode(ticket.getQrCodeUrl());
            }catch(Exception e){
                throw new NotFoundException("Ticket no encontrado");
            }
        }
        ticketRepository.delete(ticket);
    }

    @Override
    public Ticket getObject(long id) {
        return ticketRepository.findById(id).orElseThrow(()-> new NotFoundException("Ticket not found"));

    }

    @Override
    public TicketDTO.ticketResponse get(Long id) {
        return ticketMapper.toDto(getObject(id));
    }

    @Override
    public Page<TicketDTO.ticketResponse> getAll(Pageable pageable) {
        return ticketRepository.findAll(pageable).map(ticketMapper::toDto);
    }

    @Override
    public List<RouteStop> findRouteStopsByUserId(Long userId, Long tripId) {
        return ticketRepository.findRouteStopsByUserId(userId, tripId);
    }
    private String generateTicketCode() {
        String date = OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String random = UUID.randomUUID().toString()
                .substring(0, 6)
                .toUpperCase();
        return String.format("TKT-%s-%s", date, random);
    }

}
