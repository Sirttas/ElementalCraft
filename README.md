[![](http://cf.way2muchnoise.eu/395158.svg)](https://www.curseforge.com/minecraft/mc-mods/elemental-craft)
[![Discord](https://img.shields.io/discord/726853121816526878.svg?label=&logo=discord&logoColor=ffffff&color=7389D8&labelColor=6A7EC2)](https://discord.gg/BFfAmJP)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Sirttas_ElementalCraft&metric=alert_status)](https://sonarcloud.io/dashboard?id=Sirttas_ElementalCraft)

# ElementalCraft

ElementalCraft is a magic mod based around the 4 elements: fire, water, earth and air.

This project features new resources: Elements, ways to gather those elements and use them for different crafts: infusion, binding, etc. You can also use shrines to channel elements in the world and act on it for things like crop growth, turning stone into lava or extracting ores. You can also use an infuser to improve existing enchantments or add new abilities to tools.

This project is still in early development stage, a lot of features are subject to change. It is meant to be played in modpacks and try to use as much as possible features like datapack (Tags and recipes) and forge config.

You can find a presentation video [here](https://www.reddit.com/r/feedthebeast/comments/in2ask/elemental_craft_first_release_forge_1152_and_1162/).

### Simple startup guide:
First, find Inert Crystals from ore, this is the starting point of the mod, you will need to craft contained crystals from inert ones by circling them with gold nuggets. With them craft two tanks an extractor, an infuser and some pipes.

Once you got all of these start exploring and find an element source put the extractor under it and the tank under the extractor. then put the second tank nearby and the infuser on top of it, connect them with pipes and right click the pipe section connected to the extractor's tank. You can now put an inert crystal in the infuser by right clicking it to craft an infused crystal.


Elemental Craft is hosted on [modmaven](https://modmaven.dev/) add this to your `build.gradle`:
```grouvy
repositories {
    maven { url 'https://modmaven.dev/' }
}

dependencies {
    compileOnly fg.deobf("sirttas.elementalcraft:ElementalCraft:${elementalcraft_version}:api")
    runtimeOnly fg.deobf("sirttas.elementalcraft:ElementalCraft:${elementalcraft_version}")
}
```
You will also need to add dependency to [DataPack Anvil](https://github.com/Sirttas/DataPack-Anvil).


[![discord](https://i.imgur.com/mANW7ms.png "discord")](https://discord.gg/BFfAmJP "")
