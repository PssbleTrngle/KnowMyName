package com.possible_triangle.knowmyname.mixin;

import com.possible_triangle.knowmyname.KnowMyNameMod;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntity.class)
public class BlockEntityMixin {

    @Inject(at = @At("HEAD"), method = "toUpdatePacket()Lnet/minecraft/network/Packet;", cancellable = true)
    public void toUpdatePacket(CallbackInfoReturnable<Packet<ClientPlayPacketListener>> callback) {
        var self = (BlockEntity) (Object) (this);
        KnowMyNameMod.updateNBT(self)
                .map(nbt -> BlockEntityUpdateS2CPacket.create(self, $ -> nbt))
                .ifPresent(callback::setReturnValue);
    }

    @Inject(at = @At("HEAD"), method = "toInitialChunkDataNbt()Lnet/minecraft/nbt/NbtCompound;", cancellable = true)
    public void toInitialChunkDataNbt(CallbackInfoReturnable<NbtCompound> callback) {
        var self = (BlockEntity) (Object) (this);
        KnowMyNameMod.updateNBT(self).ifPresent(callback::setReturnValue);
    }

}
