package com.duong.salesmanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI hotelManagerOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Development Server");

        Contact contact = new Contact();
        contact.setName("Hotel Manager Team");
        contact.setEmail("support@hotelmanager.com");

        Info info = new Info()
                .title("Hotel Manager System API")
                .version("1.0.0")
                .description("RESTful API cho Hệ thống Quản lý Khách sạn.\n\n" +
                        "## Các chức năng chính:\n" +
                        "- **Quản lý khách hàng** (Guest Management)\n" +
                        "- **Quản lý phòng** (Room Management)\n" +
                        "- **Quản lý đặt phòng** (Reservation Management)\n" +
                        "- **Quản lý dịch vụ** (Service Management)\n" +
                        "- **Quản lý hóa đơn & thanh toán** (Invoice & Payment)")
                .contact(contact)
                .license(new License().name("MIT License").url("https://opensource.org/licenses/MIT"));

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer));
    }
}
