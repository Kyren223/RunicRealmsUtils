package me.kyren223.rrutils.mixin;

import me.kyren223.rrutils.utils.Utils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Inject(method = "dropSelectedItem", at = @At("HEAD"), cancellable = true)
    private void dropSelectedItem(boolean entireStack, CallbackInfoReturnable<Boolean> cir) {
        if (!Utils.getPlayer().isSneaking()) {
            Utils.sendMessage("Drop Stopped - Hold SHIFT + Q to drop this item", Formatting.RED);
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
