package com.brok.service;

import com.brok.entity.Asset;
import com.brok.entity.User;
import com.brok.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssetService {
    private final AssetRepository assetRepository;
    private final UserService userService;


    @Transactional
    public Asset save(Asset asset,String username) {

        Optional<Asset> optionalAsset = assetRepository.getAsset(asset.getSymbol(), username);
        if (optionalAsset.isPresent()) {
            optionalAsset.get().setSize(optionalAsset.get().getSize() + asset.getSize());
            asset = optionalAsset.get();
        } else {
            User user = userService.findByUsername(username);
            asset.setUser(user);
        }
        return assetRepository.save(asset);
    }


    @Transactional
    public List<Asset> findAllByUsername(String username) {
        return assetRepository.findAllByUsername(username);
    }

    @Transactional
    public Asset getAsset(String symbol,String username) {
        return assetRepository.getAsset(symbol, username).orElseThrow(() -> new IllegalArgumentException("Asset not found"));
    }

}
