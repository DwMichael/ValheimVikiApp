package com.rabbitv.valheimviki.data.mappers.weapons

import com.example.domain.entities.weapon.metaData.ammo.AmmoUpgradeInfoItem
import com.example.domain.entities.weapon.metaData.axe.AxeUpgradeInfoItem
import com.example.domain.entities.weapon.metaData.blood_magic.BloodMagicUpgradeInfoItem
import com.example.domain.entities.weapon.metaData.clubs.ClubUpgradeInfoItem
import com.example.domain.entities.weapon.metaData.clubs.bow.BowUpgradeInfoItem
import com.example.domain.entities.weapon.metaData.crossbow.CrossbowUpgradeInfoItem
import com.example.domain.entities.weapon.metaData.elemental_magic.ElementalMagicUpgradeInfoItem
import com.example.domain.entities.weapon.metaData.fist.FistUpgradeInfoItem
import com.example.domain.entities.weapon.metaData.knife.KnifeUpgradeInfoItem
import com.example.domain.entities.weapon.metaData.polearm.PolearmUpgradeInfoItem
import com.example.domain.entities.weapon.metaData.shield.ShieldUpgradeInfoItem
import com.example.domain.entities.weapon.metaData.spear.SpearUpgradeInfoItem
import com.example.domain.entities.weapon.metaData.sword.SwordUpgradeInfoItem
import com.rabbitv.valheimviki.domain.model.weapon.UpgradeInfo


fun UpgradeInfo.toAmmoUpgradeInfo(): AmmoUpgradeInfoItem {
    return AmmoUpgradeInfoItem(
        pierceDamage = this.pierceDamage
    )
}

fun UpgradeInfo.toAxeUpgradeInfoItem(): AxeUpgradeInfoItem {
    return AxeUpgradeInfoItem(
        slashDamage = this.slashDamage,
        poisonDamage = this.poisonDamage,
        spiritDamage = this.spiritDamage,
        upgradeLevels = this.upgradeLevels,
        chopTreesDamage = this.chopTreesDamage,
        durability = this.durability,
        stationLevel = this.stationLevel
    )
}

fun UpgradeInfo.toBloodMagicUpgradeInfoItem(): BloodMagicUpgradeInfoItem {
    return BloodMagicUpgradeInfoItem(
        chopDamage = this.chopDamage,
        fireDamage = this.fireDamage,
        pureDamage = this.pureDamage,
        pickaxeDamage = this.pickaxeDamage,
        upgradeLevels = this.upgradeLevels,
        durability = this.durability,
        stationLevel = this.stationLevel,
        damageAbsorbedBloodMagic0 = this.damageAbsorbedBloodMagic0,
        maximumSkeletonsControllable = this.maximumSkeletonsControllable,
        damageAbsorbedBloodMagic100 = this.damageAbsorbedBloodMagic100
    )
}

fun UpgradeInfo.toBowUpgradeInfoItem(): BowUpgradeInfoItem {
    return BowUpgradeInfoItem(
        poisonDamage = this.poisonDamage,
        spiritDamage = this.spiritDamage,
        upgradeLevels = this.upgradeLevels,
        durability = this.durability,
        stationLevel = this.stationLevel,
        pierceDamage = this.pierceDamage
    )
}

fun UpgradeInfo.toClubUpgradeInfoItem(): ClubUpgradeInfoItem {
    return ClubUpgradeInfoItem(
        fireDamage = this.fireDamage,
        bluntDamage = this.bluntDamage,
        frostDamage = this.frostDamage,
        spiritDamage = this.spiritDamage,
        upgradeLevels = this.upgradeLevels,
        durability = this.durability,
        stationLevel = this.stationLevel,
        pierceDamage = this.pierceDamage
    )
}

fun UpgradeInfo.toCrossbowUpgradeInfoItem(): CrossbowUpgradeInfoItem {
    return CrossbowUpgradeInfoItem(
        pierceDamage = this.pierceDamage,
        upgradeLevels = this.upgradeLevels,
        durability = this.durability,
        stationLevel = this.stationLevel,
    )
}

fun UpgradeInfo.toElementalMagicUpgradeInfoItem(): ElementalMagicUpgradeInfoItem {
    return ElementalMagicUpgradeInfoItem(
        fireDamage = this.fireDamage,
        bluntDamage = this.bluntDamage,
        frostDamage = this.frostDamage,
        poisonDamage = this.poisonDamage,
        upgradeLevels = this.upgradeLevels,
        lightningDamage = this.lightningDamage,
        durability = this.durability,
        stationLevel = this.stationLevel
    )
}

fun UpgradeInfo.toFistUpgradeInfoItem(): FistUpgradeInfoItem {
    return FistUpgradeInfoItem(
        slashDamage = this.slashDamage,
        upgradeLevels = this.upgradeLevels,
        durability = this.durability,
        stationLevel = this.stationLevel
    )
}

fun UpgradeInfo.toKnifeUpgradeInfoItem(): KnifeUpgradeInfoItem {
    return KnifeUpgradeInfoItem(
        slashDamage = this.slashDamage,
        pierceDamage = this.pierceDamage,
        spiritDamage = this.spiritDamage,
        upgradeLevels = this.upgradeLevels,
        durability = this.durability,
        stationLevel = this.stationLevel
    )
}

fun UpgradeInfo.toPolearmUpgradeInfoItem(): PolearmUpgradeInfoItem {
    return PolearmUpgradeInfoItem(
        lightningDamage = this.lightningDamage,
        upgradeLevels = this.upgradeLevels,
        durability = this.durability,
        stationLevel = this.stationLevel,
        pierceDamage = this.pierceDamage,
    )
}

fun UpgradeInfo.toShieldUpgradeInfoItem(): ShieldUpgradeInfoItem {
    return ShieldUpgradeInfoItem(
        spiritDamage = this.spiritDamage,
        upgradeLevels = this.upgradeLevels,
        durability = this.durability,
        stationLevel = this.stationLevel,

        )
}

fun UpgradeInfo.toSpearUpgradeInfoItem(): SpearUpgradeInfoItem {
    return SpearUpgradeInfoItem(
        upgradeLevels = this.upgradeLevels,
        durability = this.durability,
        stationLevel = this.stationLevel,
        pierceDamage = this.pierceDamage,
    )
}

fun UpgradeInfo.toSwordUpgradeInfoItem(): SwordUpgradeInfoItem {
    return SwordUpgradeInfoItem(
        fireDamage = this.fireDamage,
        frostDamage = this.frostDamage,
        slashDamage = this.slashDamage,
        spiritDamage = this.spiritDamage,
        upgradeLevels = this.upgradeLevels,
        durability = this.durability,
        stationLevel = this.stationLevel,
    )
}


fun AmmoUpgradeInfoItem.toUpgradeInfo(): UpgradeInfo = UpgradeInfo(
    pierceDamage = pierceDamage
)


fun AxeUpgradeInfoItem.toUpgradeInfo(): UpgradeInfo = UpgradeInfo(
    slashDamage = slashDamage,
    poisonDamage = poisonDamage,
    spiritDamage = spiritDamage,
    upgradeLevels = upgradeLevels,
    chopTreesDamage = chopTreesDamage,
    durability = durability,
    stationLevel = stationLevel
)


fun BloodMagicUpgradeInfoItem.toUpgradeInfo(): UpgradeInfo = UpgradeInfo(
    chopDamage = chopDamage,
    fireDamage = fireDamage,
    pureDamage = pureDamage,
    pickaxeDamage = pickaxeDamage,
    upgradeLevels = upgradeLevels,
    durability = durability,
    stationLevel = stationLevel,
    damageAbsorbedBloodMagic0 = damageAbsorbedBloodMagic0,
    maximumSkeletonsControllable = maximumSkeletonsControllable,
    damageAbsorbedBloodMagic100 = damageAbsorbedBloodMagic100
)


fun BowUpgradeInfoItem.toUpgradeInfo(): UpgradeInfo = UpgradeInfo(
    poisonDamage = poisonDamage,
    spiritDamage = spiritDamage,
    upgradeLevels = upgradeLevels,
    durability = durability,
    stationLevel = stationLevel,
    pierceDamage = pierceDamage
)


fun ClubUpgradeInfoItem.toUpgradeInfo(): UpgradeInfo = UpgradeInfo(
    fireDamage = fireDamage,
    bluntDamage = bluntDamage,
    frostDamage = frostDamage,
    spiritDamage = spiritDamage,
    upgradeLevels = upgradeLevels,
    durability = durability,
    stationLevel = stationLevel,
    pierceDamage = pierceDamage
)


fun CrossbowUpgradeInfoItem.toUpgradeInfo(): UpgradeInfo = UpgradeInfo(
    pierceDamage = pierceDamage,
    upgradeLevels = upgradeLevels,
    durability = durability,
    stationLevel = stationLevel
)


fun ElementalMagicUpgradeInfoItem.toUpgradeInfo(): UpgradeInfo = UpgradeInfo(
    fireDamage = fireDamage,
    bluntDamage = bluntDamage,
    frostDamage = frostDamage,
    poisonDamage = poisonDamage,
    lightningDamage = lightningDamage,
    upgradeLevels = upgradeLevels,
    durability = durability,
    stationLevel = stationLevel
)


fun FistUpgradeInfoItem.toUpgradeInfo(): UpgradeInfo = UpgradeInfo(
    slashDamage = slashDamage,
    upgradeLevels = upgradeLevels,
    durability = durability,
    stationLevel = stationLevel
)


fun KnifeUpgradeInfoItem.toUpgradeInfo(): UpgradeInfo = UpgradeInfo(
    slashDamage = slashDamage,
    pierceDamage = pierceDamage,
    spiritDamage = spiritDamage,
    upgradeLevels = upgradeLevels,
    durability = durability,
    stationLevel = stationLevel
)


fun PolearmUpgradeInfoItem.toUpgradeInfo(): UpgradeInfo = UpgradeInfo(
    lightningDamage = lightningDamage,
    pierceDamage = pierceDamage,
    upgradeLevels = upgradeLevels,
    durability = durability,
    stationLevel = stationLevel
)


fun ShieldUpgradeInfoItem.toUpgradeInfo(): UpgradeInfo = UpgradeInfo(
    spiritDamage = spiritDamage,
    upgradeLevels = upgradeLevels,
    durability = durability,
    stationLevel = stationLevel
)


fun SpearUpgradeInfoItem.toUpgradeInfo(): UpgradeInfo = UpgradeInfo(
    pierceDamage = pierceDamage,
    upgradeLevels = upgradeLevels,
    durability = durability,
    stationLevel = stationLevel
)


fun SwordUpgradeInfoItem.toUpgradeInfo(): UpgradeInfo = UpgradeInfo(
    fireDamage = fireDamage,
    frostDamage = frostDamage,
    slashDamage = slashDamage,
    spiritDamage = spiritDamage,
    upgradeLevels = upgradeLevels,
    durability = durability,
    stationLevel = stationLevel
)


fun List<UpgradeInfo>.toAmmoUpgradeInfoList(): List<AmmoUpgradeInfoItem> {
    return this.map { it.toAmmoUpgradeInfo() }
}

fun List<UpgradeInfo>.toAxeUpgradeInfoItemList(): List<AxeUpgradeInfoItem> {
    return this.map { it.toAxeUpgradeInfoItem() }
}

fun List<UpgradeInfo>.toBloodMagicUpgradeInfoItemList(): List<BloodMagicUpgradeInfoItem> {
    return this.map { it.toBloodMagicUpgradeInfoItem() }
}

fun List<UpgradeInfo>.toBowUpgradeInfoItemList(): List<BowUpgradeInfoItem> {
    return this.map { it.toBowUpgradeInfoItem() }
}

fun List<UpgradeInfo>.toClubUpgradeInfoItemList(): List<ClubUpgradeInfoItem> {
    return this.map { it.toClubUpgradeInfoItem() }
}

fun List<UpgradeInfo>.toCrossbowUpgradeInfoItemList(): List<CrossbowUpgradeInfoItem> {
    return this.map { it.toCrossbowUpgradeInfoItem() }
}

fun List<UpgradeInfo>.toElementalMagicUpgradeInfoItemList(): List<ElementalMagicUpgradeInfoItem> {
    return this.map { it.toElementalMagicUpgradeInfoItem() }
}

fun List<UpgradeInfo>.toFistUpgradeInfoItemList(): List<FistUpgradeInfoItem> {
    return this.map { it.toFistUpgradeInfoItem() }
}

fun List<UpgradeInfo>.toKnifeUpgradeInfoItemList(): List<KnifeUpgradeInfoItem> {
    return this.map { it.toKnifeUpgradeInfoItem() }
}

fun List<UpgradeInfo>.toPolearmUpgradeInfoItemList(): List<PolearmUpgradeInfoItem> {
    return this.map { it.toPolearmUpgradeInfoItem() }
}

fun List<UpgradeInfo>.toShieldUpgradeInfoItemList(): List<ShieldUpgradeInfoItem> {
    return this.map { it.toShieldUpgradeInfoItem() }
}

fun List<UpgradeInfo>.toSpearUpgradeInfoItemList(): List<SpearUpgradeInfoItem> {
    return this.map { it.toSpearUpgradeInfoItem() }
}

fun List<UpgradeInfo>.toSwordUpgradeInfoItemList(): List<SwordUpgradeInfoItem> {
    return this.map { it.toSwordUpgradeInfoItem() }
}
