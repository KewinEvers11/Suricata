package org.oyabun.suricata.services;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.oyabun.suricata.mappers.SolicitudMonitoreoMapper;
import org.oyabun.suricata.repositories.SolicitudMonitoreoRepository;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SolicitudMonitoreoServiceImplTest {

    @Mock
    SolicitudMonitoreoRepository solicitudMonitoreoRepository;

    @Spy
    SolicitudMonitoreoMapper solicitudMonitoreoMapper = Mappers.getMapper(SolicitudMonitoreoMapper.class);



}