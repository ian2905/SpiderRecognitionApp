package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test.Database.AppDatabase;
import com.example.test.Database.Entry;
import com.example.test.Database.EntryDao;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

public class DisplayActivity extends AppCompatActivity {

    //Wikipedia info
    Hashtable<String, String> urls = new Hashtable<String, String>();
    Hashtable<String, String> tempDescriptions = new Hashtable<String, String>();

    private Button displayHomeButton;
    private Button displayGalleryButton;
    private Button displayAboutButton;
    private Button displayDeleteButton;
    private Button displayChangeNameButton;
    private Button displayWikiLinkButton;

    private TextView displayPhotoName;
    private TextView displaySpeciesName;
    private TextView displaySpeciesDescription;
    private TextView displayProbability;
    private ImageView displayPhoto;

    Entry entry;
    int photoID;

    AppDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        db = AppDatabase.getInstance(DisplayActivity.this);

        setUrls();
        setDescriptions();

        // Get view instances
        displayHomeButton = (Button)findViewById(R.id.displayHomeButton);
        displayGalleryButton = (Button)findViewById(R.id.displayGalleryButton);
        displayAboutButton = (Button)findViewById(R.id.displayAboutButton);
        displayDeleteButton = (Button)findViewById(R.id.displayDeleteButton);
        displayChangeNameButton = (Button)findViewById(R.id.displayChangeNameButton);
        displayWikiLinkButton = (Button)findViewById(R.id.displayWikiLinkButton);

        displayPhotoName = (TextView)findViewById(R.id.displayPhotoName);
        displaySpeciesName = (TextView)findViewById(R.id.displaySpeciesName);
        displaySpeciesDescription = (TextView)findViewById(R.id.displaySpeciesDescription);
        displayPhoto = (ImageView)findViewById(R.id.displayPhoto);
        displayProbability = (TextView)findViewById(R.id.displayProbability);

        displaySpeciesDescription.setMovementMethod(new ScrollingMovementMethod());
        displayWikiLinkButton.setMovementMethod(new LinkMovementMethod());

        // Set onClicks
        displayHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(DisplayActivity.this, HomeActivity.class);
                startActivity(homeIntent);
                finish();
            }
        });
        displayGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(DisplayActivity.this, GalleryActivity.class);
                startActivity(galleryIntent);
                finish();
            }
        });
        displayAboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aboutIntent = new Intent(DisplayActivity.this, AboutActivity.class);
                startActivity(aboutIntent);
                finish();
            }
        });
        displayDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
        displayChangeNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeName();
            }
        });
        displayWikiLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(displayWikiLinkButton.getText().toString()));
                startActivity(i);
            }
        });

        //get the ID from the intent data
        Intent i = this.getIntent();
        photoID = i.getIntExtra("photoID", photoID);

        //pull the entry from the database
        EntryDao entryDao = db.entryDao();
        entry = entryDao.getEntry(photoID);

        // Grab entry information and populate views
        displayPhotoName.setText(entry.photoName);
        if(entry.speciesName != null){
            displaySpeciesName.setText(entry.speciesName);
        }
        if(entry.description != null){
            displaySpeciesDescription.setText(entry.description);
        }
        if(entry.probability != null){
            displayProbability.setText(String.format("%.2f", entry.probability));
        }
        displayPhoto.setImageBitmap(BitmapFactory.decodeFile(entry.path));
        displayWikiLinkButton.setText(displayWikiLinkButton.getText().toString() + urls.get(entry.speciesName));
        displaySpeciesDescription.setText(tempDescriptions.get(entry.speciesName));
    }

    // Deletes the current entry from the database
    private void delete(){
        EntryDao entryDao = db.entryDao();
        entryDao.delete(entry);

        Intent galleryIntent = new Intent(DisplayActivity.this, GalleryActivity.class);
        startActivity(galleryIntent);
    }

    // Changes name of entry in the database
    private void changeName(){
        Intent changeNameIntent = new Intent(DisplayActivity.this, ChangeName.class);
        changeNameIntent.putExtra("photoID", entry.photoID);
        startActivity(changeNameIntent);
        finish();
    }

    // By Wando located https://stackoverflow.com/questions/53915588/extract-text-from-website-in-android-studio/53915635
    // Unused method to dynamically grab the contents of the necessary wikipedia page, future work
    private void getBodyText(String species) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();

                try {
                    String url="https://en.wikipedia.org/wiki/" + species;
                    Document doc = Jsoup.connect(url).get();

                    Element body = doc.body();
                    builder.append(body.text());

                } catch (Exception e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        displaySpeciesDescription.setText(builder.toString());
                    }
                });
            }
        }).start();
    }

    private void setUrls(){
        //setup wikipedia urls
        urls.put("Tarantula", "Tarantula");
        urls.put("Widow Spider", "Latrodectus");
        urls.put("Long-Jawed Orb Weaver", "Long-jawed_orb_weaver");
        urls.put("Jumping Spider", "Jumping_spider");
        urls.put("Nursery Web Spider", "Nursery_web_spider");
        urls.put("Cellar Spider", "Pholcidae");
        urls.put("Orb-Weaver Spider", "Orb-weaver_spider");
        urls.put("Lynx Spider", "Lynx_spider");
        urls.put("Huntsman Spider", "Huntsman_spider");
        urls.put("Tangle-Web Spider", "Theridiidae");
        urls.put("Crab Spider", "Thomisidae");
    }
    private void setDescriptions(){
        tempDescriptions.put("Tarantula", "Tarantulas comprise a group of large and often hairy spiders of the family Theraphosidae. Currently, 1,010 species have been identified. The term \"tarantula\" is usually used to describe members of the family Theraphosidae, although many other members of the same infraorder (Mygalomorphae) are commonly referred to as \"tarantulas\" or \"false tarantulas\". Some of the more common species have become popular in the exotic pet trade. Many New World species kept as pets have setae known as urticating hairs that can cause irritation to the skin, and in extreme cases, cause damage to the eyes");
        tempDescriptions.put("Widow Spider", "Latrodectus is a broadly distributed genus of spiders with several species that, together, are commonly known[citation needed] as true widows. This group is composed of those often loosely called black widow spiders, brown widow spiders, and similar spiders. However, such general \"common names\" are of limited use, as the diversity of species is much greater. A member of the family Theridiidae, this genus contains 34 species, which include several North American \"black widows\" (southern black widow Latrodectus mactans, western black widow Latrodectus hesperus, and northern black widow Latrodectus variolus). Besides these, North America also has the red widow Latrodectus bishopi and the brown widow Latrodectus geometricus, which, in addition to North America, has a much wider geographic distribution. Elsewhere, others include the European black widow (Latrodectus tredecimguttatus), the Australian redback black widow (Latrodectus hasseltii) and the closely-related New Zealand katipō (Latrodectus katipo), several different species in Southern Africa that can be called Button spiders, and the South American black-widow spiders (Latrodectus corallinus and Latrodectus curacaviensis). Species vary widely in size. In most cases, the females are dark-coloured, but some may have lighter bodies or even reddish. Many can have red, white or brown markings on the upper-side (dorsal) of the abdomen. Some can be readily identifiable by reddish markings on the central underside (ventral) abdomen, which are often hourglass-shaped.");
        tempDescriptions.put("Long-Jawed Orb Weaver", "Long-jawed orb weavers or long jawed spiders (Tetragnathidae) are a family of araneomorph spiders first described by Anton Menge in 1866. They have elongated bodies, legs, and chelicerae, and build small orb webs with an open hub with few, wide-set radii and spirals with no signal line or retreat. Some species are often found in long vegetation near water.");
        tempDescriptions.put("Jumping Spider", "Jumping spiders or the Salticidae are a family of spiders. As of 2019, it contained over 600 described genera and over 6,000 described species, making it the largest family of spiders at 13% of all species. Jumping spiders have some of the best vision among arthropods and use it in courtship, hunting, and navigation. Although they normally move unobtrusively and fairly slowly, most species are capable of very agile jumps, notably when hunting, but sometimes in response to sudden threats or crossing long gaps. Both their book lungs and tracheal system are well-developed, and they use both systems (bimodal breathing). Jumping spiders are generally recognized by their eye pattern. All jumping spiders have four pairs of eyes, with the anterior median pair being particularly large.");
        tempDescriptions.put("Nursery Web Spider", "Nursery web spiders (Pisauridae) is a family of araneomorph spiders first described by Eugène Simon in 1890. They resemble wolf spiders (Lycosidae) except for several key differences. Wolf spiders have two very prominent eyes in addition to the other six, while a nursery web spider's eyes are all about the same size. Additionally, female nursery web spiders carry their egg sacs with their jaws and pedipalps instead of attaching them to their spinnerets as wolf spiders do. When the eggs are about to hatch, a female spider builds a nursery \"tent\", places her egg sac inside, and stands guard outside, hence the family's common name. Like the wolf spiders, however, the nursery web spiders are roaming hunters that don't use webs for catching prey.");
        tempDescriptions.put("Cellar Spider", "The Pholcidae are a family of araneomorph spiders. The family contains over 1,800  individual species of pholcids, including those commonly known as the marbled cellar spider (Holocnemus pluchei), daddy long-legs spider, carpenter spider, daddy long-legger, vibrating spider, gyrating spider, long daddy, and skull spider. The family, first described by Carl Ludwig Koch in 1850,[1] is divided into 94 genera");
        tempDescriptions.put("Orb-Weaver Spider", "Orb-weaver spiders are members of the spider family Araneidae. They are the most common group of builders of spiral wheel-shaped webs often found in gardens, fields, and forests. The English word orb can mean \"circular\", hence the English name of the group. Araneids have eight similar eyes, hairy or spiny legs, and no stridulating organs.");
        tempDescriptions.put("Lynx Spider", "Lynx spider (Oxyopidae) is a family of araneomorph spiders first described by Tamerlan Thorell in 1870. Most species make little use of webs, instead spending their lives as hunting spiders on plants. Many species frequent flowers in particular, ambushing pollinators, much as crab spiders do. They tend to tolerate members of their own species more than most spiders do, and at least one species has been identified as exhibiting social behaviour.");
        tempDescriptions.put("Huntsman Spider", "Huntsman spiders, members of the family Sparassidae (formerly Heteropodidae), are known by this name because of their speed and mode of hunting.[citation needed] They are also called giant crab spiders because of their size and appearance. Larger species sometimes are referred to as wood spiders, because of their preference for woody places (forests, mine shafts, woodpiles, wooden shacks). In southern Africa the genus Palystes are known as rain spiders or lizard-eating spiders. Commonly, they are confused with baboon spiders from the Mygalomorphae infraorder, which are not closely related.");
        tempDescriptions.put("Tangle-Web Spider", "Theridiidae, also known as the tangle-web spiders, cobweb spiders and comb-footed spiders, is a large family of araneomorph spiders first described by Carl Jakob Sundevall in 1833. This diverse, globally distributed family includes over 3,000 species in 124 genera, and is the most common arthropod found in human dwellings throughout the world.");
        tempDescriptions.put("Crab Spider", "The Thomisidae are a family of spiders, including about 170 genera and over 2,100 species. The common name crab spider is often linked to species in this family, but is also applied loosely to many other families of spiders. Many members of this family are also known as flower spiders or flower crab spiders.");
    }

}