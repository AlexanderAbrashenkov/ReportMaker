package pro.bigbro.restTemplates;

import pro.bigbro.models.clients.Client;

import java.util.List;

public interface ClientRestTemplate {
    List<Client> getAllClients();
}
