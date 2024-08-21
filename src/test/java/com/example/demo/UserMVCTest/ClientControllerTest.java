package com.example.demo.UserMVCTest;

import com.example.demo.Controller.ClientController;
import com.example.demo.Models.Client;
import com.example.demo.Service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ClientController.class)
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateClient() throws Exception {
        Client client = new Client();
        client.setClientId(1);  // Updated to match your actual field name
        client.setClientName("Test Client");  // Updated to match your actual field name

        when(clientService.createClient(any(Client.class))).thenReturn(client);

        mockMvc.perform(post("/clients/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value(1))  // Updated JSON path
                .andExpect(jsonPath("$.clientName").value("Test Client"));  // Updated JSON path
    }

    @Test
    void testGetAllClients() throws Exception {
        Client client1 = new Client();
        client1.setClientId(1);
        client1.setClientName("Client 1");

        Client client2 = new Client();
        client2.setClientId(2);
        client2.setClientName("Client 2");

        List<Client> clients = Arrays.asList(client1, client2);

        when(clientService.getAllClients()).thenReturn(clients);

        mockMvc.perform(get("/clients/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clientId").value(1))
                .andExpect(jsonPath("$[0].clientName").value("Client 1"))
                .andExpect(jsonPath("$[1].clientId").value(2))
                .andExpect(jsonPath("$[1].clientName").value("Client 2"));
    }
}
