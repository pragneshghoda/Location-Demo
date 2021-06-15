package com.android.pomelotest.pickup_locations.controller.model

import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.android.pomelotest.R
import com.android.pomelotest.base.epoxy.EpoxyBaseViewHolder
import com.android.pomelotest.base.model.Pickup
import kotlinx.android.synthetic.main.list_item_pickup_locations.view.*

@EpoxyModelClass(layout = R.layout.list_item_pickup_locations)
abstract class PickupLocationModel : EpoxyModelWithHolder<EpoxyBaseViewHolder>() {
    @EpoxyAttribute
    lateinit var location: Pickup

    override fun bind(holder: EpoxyBaseViewHolder) {
        holder.itemView.apply {
            with(location) {
                textViewAlias.text = alias
                textViewDistance.text = when {
                    distance.isNaN() -> context.getString(R.string.pickup_empty_distance)
                    else -> context.getString(R.string.pickup_distance, distance)
                }
                textViewAddress.text = address1.takeIf { !it.isEmpty() } ?: address2
                textViewCity.text = city
            }
        }
    }
}