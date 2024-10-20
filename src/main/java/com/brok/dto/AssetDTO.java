package com.brok.dto;

import com.brok.entity.Asset;
import lombok.Data;

@Data
public class AssetDTO {
    private int size;
    private String symbol;
    public Asset toAsset() {
        return Asset.builder().symbol(this.symbol).size(this.size).build();
    }
}
