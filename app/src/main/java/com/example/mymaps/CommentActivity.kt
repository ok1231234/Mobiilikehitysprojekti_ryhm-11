package com.example.mymaps

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mymaps.adapters.ViewPagerAdapter
import com.example.mymaps.databinding.ActivityCommentBinding
import com.google.firebase.database.*

class CommentActivity : AppCompatActivity() {
    // lateinit var productList: MutableList<Reviews>
    var markerdata: String = ""
    private lateinit var binding: ActivityCommentBinding
    lateinit var ref: DatabaseReference

    //  lateinit var listView: ListView
    lateinit var kommenttiteksti: String
    var arvosanatahti: Double = 0.0

    data class RestaurantFeedItem(
        val title: String = "",
        val description: String = ""
    )


    data class kommentti(
        val arvosana: Double = 0.0,
        val palaute: String = ""
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        markerdata = intent.getStringExtra("Ravintola")!!

        //  markerdata = intent.getStringExtra("Ravintola")!!
        ref = FirebaseDatabase.getInstance().getReference("Ravintolat").child(markerdata)
            .child("Kommentit")
        //productList = mutableListOf()

        ref.addValueEventListener(object : ValueEventListener {


//            fun ajetaanarvo(){
//                kommenttiteksti = "Ihan jees, tää tuli ohjelmasta"
//                arvosanatahti = 3.5
//                val productId: String? = ref.push().key
//                val item = kommentti(arvosanatahti, kommenttiteksti)
//
//                ref.child(productId.toString()).setValue(item).addOnCompleteListener{
//                    Toast.makeText(applicationContext, "Kommentti jätetty", Toast.LENGTH_LONG).show()
//                }
//            }


            override fun onCancelled(error: DatabaseError) {

            }

            // *YLLÄ* tietokantaan viittaavaan ref-muuttujaan asetetaan tietynlainen tiedonkuuntelija
            // jonka avulla tietokannasta haetaan snapshot *ALLA* eli hakutulos
            // olemassaolevista kentistä ja asetetaan tiedon näkymän toteuttavan ja
            // oikeassa muodossa tulostavan adapterin kautta sille tarkoitetulle listalle.

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot!!.exists()) {
                    //       productList.clear()
                    Log.d("SNAPSHOT", snapshot.value.toString())
                    Log.d("SssssNAPSHOT", markerdata)
                    Log.d("markerdata", markerdata)


                    val moro: List<kommentti> = snapshot.children.map { Moro ->
                        Moro.getValue(kommentti::class.java)!!
                    }

                    val RestaurantFeedItems: List<RestaurantFeedItem> =
                        snapshot.children.map { Moro ->
                            Moro.getValue(RestaurantFeedItem::class.java)!!
                        }
                    Log.d("ITEMS", RestaurantFeedItems.toString())
                    Log.d("ITEMSS", moro.toString())
//                    for(h in snapshot.children){
//                        val product = h.getValue(RestaurantFeedItem::class.java)
//                        Log.d("kommentti", product.toString())
////                        productList.add(product!!)
//                }

//                    for(h in snapshot.children){
//                        val product = h.getValue(Reviews::class.java)
//                        productList.add(product!!)
//                    }
//                    val adapter = ProductAdapter(this@CommentActivity, R.layout.single_item, productList)
//                    //listView.adapter = adapter
                }
            }

        });

        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)
        val view = binding.root

        //setContentView(R.layout.activity_comment)
        setContentView(view)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setUpTabs()

        var namiska = findViewById(R.id.button4) as Button
        namiska.setOnClickListener {
            kommenttiteksti = "Ihan jees, tää tuli ohjelmasta"
            arvosanatahti = 3.5
            val productId: String? = ref.push().key
            val item = kommentti(arvosanatahti, kommenttiteksti)

            ref.child(productId.toString()).setValue(item).addOnCompleteListener {
                Toast.makeText(applicationContext, "Kommentti jätetty", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_bar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.icLogOut)?.setVisible(false)
        menu?.findItem(R.id.icLogin)?.setVisible(false)
        menu?.findItem(R.id.profile)?.setVisible(false)
        return super.onPrepareOptionsMenu(menu)
    }

    private fun setUpTabs() {
        val adapter = ViewPagerAdapter(supportFragmentManager)

        //val markerdata = intent.getStringExtra("Ravintola")


        //  binding.moro.setText(moro)
        // Log.d("Firebase lista" , productList.size.toString())
        adapter.addFragment(BarFrag1(), markerdata.toString())
        adapter.addFragment(BarFrag2(), "Tapahtumat")
        adapter.addFragment(BarFrag3(), "Kommentit")
        binding.viewPager.adapter = adapter
        binding.bartablayout.setupWithViewPager(binding.viewPager)


    }

}