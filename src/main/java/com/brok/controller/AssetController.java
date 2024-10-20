package com.brok.controller;

import com.brok.dto.AssetDTO;
import com.brok.entity.Asset;
import com.brok.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/v1/asset")
@RestController
public class AssetController {
    private final AssetService assetService;

    @GetMapping
    ResponseEntity<List<Asset>> getAssets(Principal principal) {
        return ResponseEntity.ok(assetService.findAllByUsername(principal.getName()));
    }

    @GetMapping("/{symbol}")
    ResponseEntity<Asset> getAsset(@PathVariable String symbol, Principal principal) {
        return ResponseEntity.ok(assetService.getAsset(symbol, principal.getName()));
    }

    @PostMapping
    ResponseEntity<Asset> createAsset(@RequestBody AssetDTO dto, Principal principal) {
        return ResponseEntity.ok(assetService.save(dto.toAsset(), principal.getName()));
    }
}
