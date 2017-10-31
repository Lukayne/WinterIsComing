package com.richardsmith.winteriscoming


import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.place_item.*


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : LifecycleFragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PlacesAdapter()
        placesList.adapter = adapter
        placesList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val viewModel = ViewModelProviders.of(activity).get(PlaceViewModel::class.java)

        val itemTouchHelper = ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START) {
            override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?,
                                target: RecyclerView.ViewHolder?): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
                val index = viewHolder?.adapterPosition ?: -1
                if (index == -1) return

                val place = adapter.places[index]
                viewModel.removePlace(place)

            }
        })
        itemTouchHelper.attachToRecyclerView(placesList)



        viewModel.allPlaces.observe(this, Observer {
            println("Success")
            (placesList.adapter as PlacesAdapter).places = it ?: emptyList()
            placesList.adapter.notifyDataSetChanged()
        })
        addPlace.setOnClickListener {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, SaveCityFragment())
                    .commit()
        }
    }

    class PlacesAdapter : RecyclerView.Adapter<PlaceViewHolder>() {
        var places: List<Place> = emptyList()

        override fun getItemCount(): Int = places.size

        override fun onBindViewHolder(holder: PlaceViewHolder?, position: Int) {
            val place = places[position]
            holder?.placeName?.text = place.city
            holder?.place = place
            holder?.itemView.setOnClickListener {
                holder?.place?.id

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PlaceViewHolder {
            val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.place_item, parent, false)
            return PlaceViewHolder(itemView)
        }

    }

    class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var place: Place? = null
        val placeName: TextView = itemView.findViewById(R.id.cityName)
    }

}// Required empty public constructor
