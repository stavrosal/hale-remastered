function startQuest(game) {
    if (game.hasQuestEntry("The Master")) return;
    
    var quest = game.getQuestEntry("The Master")
    var entry = quest.createSubEntry("The Cave Collapse")
    entry.addText("You need to find the master of the mercenaries who tried to kill you, and how it fits in with the strange visions you have had.");
    entry.addText("First, though, you need to find some way of returning to the surface.");
    
}

function goblinTrust(game) {
    var quest = game.getQuestEntry("The Master");
    
    quest.setCurrentSubEntriesCompleted();
    
    var entry = quest.createSubEntry("The Goblin City");
    entry.addText("You found some unlikely allies in a clan of goblins living underground.  When you are ready, you should speak to the goblin's leader in the south end of the city about returning to the surface.");
}

function startGate(game) {
	var quest = game.getQuestEntry("The Master");
	
	if (quest.hasSubEntry("The Gate to the Surface")) return;
	
	quest.setCurrentSubEntriesCompleted();
	
	var entry = quest.createSubEntry("The Gate to the Surface");
	
	entry.addText("The goblin chieftan has told you of a gate to the surface to the south of the goblin city.  However, in order to use the gate you will need to locate three pieces of a key.  The pieces may be found by journeying beyond the mushroom forest to the north of the goblin city.");
}