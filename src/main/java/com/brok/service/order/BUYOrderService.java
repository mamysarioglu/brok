package com.brok.service.order;

import com.brok.entity.Orders;
import com.brok.entity.User;
import com.brok.entity.Wallet;
import com.brok.event.BuyEvent;
import com.brok.exeption.AmountException;
import com.brok.repository.OrderRepository;
import com.brok.repository.UserRepository;
import com.brok.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service("BUY")
@RequiredArgsConstructor
public class BUYOrderService implements OrderProcess {
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final OrderRepository orderRepository;
    @Transactional
    @Override
    public void process(Orders order, String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        Wallet wallet = user.getWallet();
        BigDecimal totalPrice = order.getPrice().multiply(new BigDecimal(order.getSize()));

        if (wallet.getAmount().compareTo(totalPrice)<0)
            throw new AmountException("You do not have enough money.");
        order.setUser(user);
        orderRepository.save(order);
        wallet.setAmount(wallet.getAmount().subtract(totalPrice));
        wallet.setBlockAmount(wallet.getBlockAmount().add(totalPrice));
        walletRepository.save(wallet);

        BuyEvent event = new BuyEvent(this,order);
        applicationEventPublisher.publishEvent(event);
    }
}
