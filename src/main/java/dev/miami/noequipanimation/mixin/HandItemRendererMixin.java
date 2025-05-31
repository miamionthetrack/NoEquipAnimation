package dev.miami.noequipanimation.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.ToolItem;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public class HandItemRendererMixin {
	@Inject(method = "applyEquipOffset", at = @At("HEAD"), cancellable = true)
	private void modifyEquipOffset(MatrixStack matrices, Arm arm, float equipProgress, CallbackInfo ci) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.player == null) return;
		ItemStack stack = arm == client.player.getMainArm() ? client.player.getMainHandStack() : client.player.getOffHandStack();
		if (stack.getItem() instanceof ToolItem ||
				stack.getItem() instanceof RangedWeaponItem ||
				client.player.getItemCooldownManager().isCoolingDown(stack.getItem())) {
			return;
		}
		int i = arm == Arm.RIGHT ? 1 : -1;
		matrices.translate((float) i * 0.56F, -0.52F, -0.72F);
		ci.cancel();
	}
}

