package io.github.erdk.itaglocator

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

class DeviceListAdapter(private val myDataset: HashMap<String, String>) : RecyclerView.Adapter<DeviceListAdapter.ViewHolder>() {
    companion object {
        val TAG = DeviceListAdapter::class.qualifiedName
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var textView: TextView = view.findViewById(R.id.textView)
        var mac: String? = null
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): DeviceListAdapter.ViewHolder {
        // create a new view
        val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.device_detail_row, parent, false)

        // set the view's size, margins, paddings and layout parameters
        //...
        return ViewHolder(textView).apply {
            view.setOnClickListener {
                Log.d(TAG, "Clicked: " + mac)
                Toast.makeText(view.context, "Clicked:" + mac, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mac = myDataset.keys.elementAt(position)
        holder.textView.text = myDataset[holder.mac!!]
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size

}