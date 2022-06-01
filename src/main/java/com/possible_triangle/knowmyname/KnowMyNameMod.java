package com.possible_triangle.knowmyname;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Nameable;

import java.util.Optional;

public class KnowMyNameMod implements ModInitializer {
	@Override
	public void onInitialize() {
	}

	public static Optional<NbtCompound> updateNBT(BlockEntity tile) {
		if (tile instanceof Nameable nameable && nameable.hasCustomName()) {
			var nbt = tile.createNbt();
			nbt.remove("Items");
			if (nameable.hasCustomName()) return Optional.of(nbt);
		}
		return Optional.empty();
	}

}
