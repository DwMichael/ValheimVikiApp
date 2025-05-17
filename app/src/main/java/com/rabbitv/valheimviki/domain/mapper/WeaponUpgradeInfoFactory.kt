package com.rabbitv.valheimviki.domain.mapper

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
import com.rabbitv.valheimviki.data.mappers.weapons.toAmmoUpgradeInfo
import com.rabbitv.valheimviki.data.mappers.weapons.toAxeUpgradeInfoItem
import com.rabbitv.valheimviki.data.mappers.weapons.toBloodMagicUpgradeInfoItem
import com.rabbitv.valheimviki.data.mappers.weapons.toBowUpgradeInfoItem
import com.rabbitv.valheimviki.data.mappers.weapons.toClubUpgradeInfoItem
import com.rabbitv.valheimviki.data.mappers.weapons.toCrossbowUpgradeInfoItem
import com.rabbitv.valheimviki.data.mappers.weapons.toElementalMagicUpgradeInfoItem
import com.rabbitv.valheimviki.data.mappers.weapons.toFistUpgradeInfoItem
import com.rabbitv.valheimviki.data.mappers.weapons.toKnifeUpgradeInfoItem
import com.rabbitv.valheimviki.data.mappers.weapons.toPolearmUpgradeInfoItem
import com.rabbitv.valheimviki.data.mappers.weapons.toShieldUpgradeInfoItem
import com.rabbitv.valheimviki.data.mappers.weapons.toSpearUpgradeInfoItem
import com.rabbitv.valheimviki.data.mappers.weapons.toSwordUpgradeInfoItem
import com.rabbitv.valheimviki.data.mappers.weapons.toUpgradeInfo
import com.rabbitv.valheimviki.domain.model.weapon.UpgradeInfo

object WeaponUpgradeInfoFactory {

    inline fun <reified T> createFromWeaponUpgradeInfo(upgradeInfo: UpgradeInfo): T {
        return when (T::class) {
            AmmoUpgradeInfoItem::class -> upgradeInfo.toAmmoUpgradeInfo() as T
            AxeUpgradeInfoItem::class -> upgradeInfo.toAxeUpgradeInfoItem() as T
            BloodMagicUpgradeInfoItem::class -> upgradeInfo.toBloodMagicUpgradeInfoItem() as T
            BowUpgradeInfoItem::class -> upgradeInfo.toBowUpgradeInfoItem() as T
            ClubUpgradeInfoItem::class -> upgradeInfo.toClubUpgradeInfoItem() as T
            CrossbowUpgradeInfoItem::class -> upgradeInfo.toCrossbowUpgradeInfoItem() as T
            ElementalMagicUpgradeInfoItem::class -> upgradeInfo.toElementalMagicUpgradeInfoItem() as T
            FistUpgradeInfoItem::class -> upgradeInfo.toFistUpgradeInfoItem() as T
            KnifeUpgradeInfoItem::class -> upgradeInfo.toKnifeUpgradeInfoItem() as T
            PolearmUpgradeInfoItem::class -> upgradeInfo.toPolearmUpgradeInfoItem() as T
            ShieldUpgradeInfoItem::class -> upgradeInfo.toShieldUpgradeInfoItem() as T
            SpearUpgradeInfoItem::class -> upgradeInfo.toSpearUpgradeInfoItem() as T
            SwordUpgradeInfoItem::class -> upgradeInfo.toSwordUpgradeInfoItem() as T
            else -> throw IllegalArgumentException("Unsupported type: ${T::class.simpleName}")
        }
    }

    fun createWeaponUpgradeInfoFrom(any: Any): UpgradeInfo = when (any) {
        is AmmoUpgradeInfoItem -> any.toUpgradeInfo()
        is AxeUpgradeInfoItem -> any.toUpgradeInfo()
        is BloodMagicUpgradeInfoItem -> any.toUpgradeInfo()
        is BowUpgradeInfoItem -> any.toUpgradeInfo()
        is ClubUpgradeInfoItem -> any.toUpgradeInfo()
        is CrossbowUpgradeInfoItem -> any.toUpgradeInfo()
        is ElementalMagicUpgradeInfoItem -> any.toUpgradeInfo()
        is FistUpgradeInfoItem -> any.toUpgradeInfo()
        is KnifeUpgradeInfoItem -> any.toUpgradeInfo()
        is PolearmUpgradeInfoItem -> any.toUpgradeInfo()
        is ShieldUpgradeInfoItem -> any.toUpgradeInfo()
        is SpearUpgradeInfoItem -> any.toUpgradeInfo()
        is SwordUpgradeInfoItem -> any.toUpgradeInfo()
        is UpgradeInfo -> any
        else -> error("Unsupported type: ${any::class.simpleName}")
    }
}