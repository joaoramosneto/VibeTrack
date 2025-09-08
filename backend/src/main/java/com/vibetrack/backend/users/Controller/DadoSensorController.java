package com.vibetrack.backend.users.Controller;

import com.vibetrack.backend.users.DTO.SensorDTO.DadoSensorRequestDTO;
import com.vibetrack.backend.users.Entity.DadoSensor;
import com.vibetrack.backend.users.Service.DadoSensorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dados-sensores")
@CrossOrigin(origins = "http://localhost:4200")
public class DadoSensorController {

    @Autowired
    private DadoSensorService dadoSensorService;

    @PostMapping
    public ResponseEntity<DadoSensor> receberDadoSensor(@Valid @RequestBody DadoSensorRequestDTO requestDTO) {
        DadoSensor novoDado = dadoSensorService.salvarDadoSensor(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoDado);
    }
}