package com.ghostbat.cleaner

object PaymentConfig {
    // SAME AS GHOST SHIELD - YOUR WALLET
    const val YOUR_WALLET = "0xBEC61d882234d8f46594a8a2FFDa20963a0dDdD5"
    const val ETHERSCAN_API = "AETRRDJJ1J6YJF324TR4CKGYYC4UAWRGMM"
    const val USDT_CONTRACT = "0xdAC17F958D2ee523a2206206994597C13D831ec7"
    
    // 3 TIERS SAME AS GHOST SHIELD
    const val BASIC_PRICE = 3 // USDT
    const val PRO_PRICE = 7
    const val KING_PRICE = 15

    fun getQrData(): String {
        return YOUR_WALLET
    }
}