package com.example.demo.UserMockitoTest;

import com.example.demo.Models.Client;
import com.example.demo.Repository.ClientRepository;
import com.example.demo.Service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ClientServiceTest {

    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientRepository clientRepository;

    private Client client;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        client = new Client();
        client.setClientId(1);
        client.setClientName("Test Client");
    }

    @Test
    void testCreateClient() {
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        Client createdClient = clientService.createClient(client);

        assertEquals(client.getClientId(), createdClient.getClientId());
        assertEquals(client.getClientName(), createdClient.getClientName());
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void testGetAllClients() {
        List<Client> clients = Arrays.asList(client);
        when(clientRepository.findAll()).thenReturn(clients);

        List<Client> allClients = clientService.getAllClients();

        assertEquals(1, allClients.size());
        assertEquals(client.getClientId(), allClients.get(0).getClientId());
        assertEquals(client.getClientName(), allClients.get(0).getClientName());
        verify(clientRepository, times(1)).findAll();
    }
}
