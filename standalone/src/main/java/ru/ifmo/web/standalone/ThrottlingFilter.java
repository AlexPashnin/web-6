package ru.ifmo.web.standalone;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ThrottlingFilter implements ContainerRequestFilter, ContainerResponseFilter, ResourceFilter {
    private static AtomicInteger activeClients = new AtomicInteger(0);
    private static final int MAX_CLIENT_COUNT = 2;


    @Override
    public ContainerRequestFilter getRequestFilter() {
        return this;
    }

    @Override
    public ContainerResponseFilter getResponseFilter() {
        return this;
    }


    @Override
    public ContainerRequest filter(ContainerRequest request) {
        if (activeClients.incrementAndGet() > MAX_CLIENT_COUNT) {
            throw new ThrottlingException();
        }

        return request;
    }

    @Override
    public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
        activeClients.decrementAndGet();
        return response;
    }
}
