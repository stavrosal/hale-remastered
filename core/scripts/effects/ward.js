function onEffectApplied(game, effectApplied, effectParent) {
	var slot = effectApplied.getSlot();
	if (slot == null) return;
	
	var abilityID = slot.getAbilityID();
	
	var abilitiesNegated = [ "Slow", "Curse", "Deafen", "Blindness", "Freeze", "FlamingFingers" ];
	
	for (var i = 0; i < abilitiesNegated.length; i++) {
		if (abilitiesNegated[i] == abilityID) {
			effectApplied.getTarget().removeEffect(effectApplied);
			effectParent.getTarget().removeEffect(effectParent);
		}
	}
}
