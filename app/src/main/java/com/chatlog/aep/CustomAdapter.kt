package com.chatlog.aep

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class CustomAdapter(var context: Context, var event: ArrayList<Event>): BaseAdapter() {
    private class ViewHolder(row: View?) {
        var eventText: TextView
        var eventImg: ImageView
        var eventDate: TextView
        var eventTime: TextView

        init {
            this.eventText = row?.findViewById(R.id.eventText) as TextView
            this.eventImg = row?.findViewById(R.id.eventImg) as ImageView
            this.eventDate = row?.findViewById(R.id.eventDate) as TextView
            this.eventTime = row?.findViewById(R.id.eventTime) as TextView
        }
    }
    override fun getCount(): Int {
        return event.count()
    }

    override fun getItem(p0: Int): Any {
        return event[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view: View?
        var viewHolder: ViewHolder
        if(p1 == null) {
            var layout = LayoutInflater.from(context)
            view = layout.inflate(R.layout.event_item_list, p2, false)
            viewHolder = ViewHolder(view)
            view?.tag = viewHolder
        } else {
            view = p1
            viewHolder = view.tag as ViewHolder
        }
        var event: Event = getItem(p0) as Event
        viewHolder.eventText.text = event.name
        viewHolder.eventDate.text = event.date
        viewHolder.eventTime.text = event.time
        val imageUrl = "https://chatlog.ru/astronomicalevents/${event.image}"
        println(imageUrl)
        println(viewHolder.eventImg)
        Picasso.get().load(imageUrl).into(viewHolder.eventImg)

        return view as View
    }
}