package com.example.logs.service;

import com.example.logs.dto.LogRequestDTO;
import com.example.logs.dto.LogResponseDTO;
import com.example.logs.mapper.LogMapper;
import com.example.logs.model.Log;
import com.example.logs.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;

    @Override
    public LogResponseDTO crearLog(LogRequestDTO request) {
        Log log = LogMapper.toEntity(request);
        return LogMapper.toDTO(logRepository.save(log));
    }

    @Override
    public List<LogResponseDTO> obtenerTodos() {
        return logRepository.findAll()
                .stream()
                .map(LogMapper::toDTO)
                .toList();
    }
}
