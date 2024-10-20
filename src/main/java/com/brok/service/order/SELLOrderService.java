package com.brok.service.order;

import com.brok.entity.Asset;
import com.brok.entity.Orders;
import com.brok.event.SellEvent;
import com.brok.repository.AssetRepository;
import com.brok.repository.OrderRepository;
import com.brok.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service("SELL")
public class SELLOrderService implements OrderProcess {

    private final AssetRepository assetRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final OrderRepository orderRepository;

    @Override
    public void process(Orders order, String username) {
        Asset asset = assetRepository.getAsset(order.getSymbol(), username).orElseThrow();
        if (asset.getSize() < order.getSize())
            throw new IllegalArgumentException("Order symbol '" + order.getSymbol() + "' is insufficient size");
        order.setUser(asset.getUser());
        order = orderRepository.save(order);

        //Block Asset
        asset.setSize(asset.getSize() - order.getSize());
        asset.setBlockSize(asset.getBlockSize() + order.getSize());
        assetRepository.save(asset);

        SellEvent event = new SellEvent(this, order);
        applicationEventPublisher.publishEvent(event);
    }
}
