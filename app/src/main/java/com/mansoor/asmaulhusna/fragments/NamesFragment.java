package com.mansoor.asmaulhusna.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TimePicker;
import android.widget.Toast;

import com.aniket.mutativefloatingactionbutton.MutativeFab;
import com.mansoor.asmaulhusna.R;
import com.mansoor.asmaulhusna.adapters.AlarmReceiver;
import com.mansoor.asmaulhusna.adapters.NameAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;


public class NamesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int position = 1;
    private String scrollButtonStatus = "idle";
    private String playButtonStatus = "idle";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView mRecyclerView;
    private NameAdapter namesAdapter;
    private List<String> nameList;
    private OnFragmentInteractionListener mListener;
    private MutativeFab scrollButton;
    private MediaPlayer mPlayer;
    private Menu menu;
    private Timer T;
    private SharedPreferences prefs;
    SharedPreferences.Editor editor;
    public NamesFragment() {
        // Required empty public constructor
    }

    public static NamesFragment newInstance(String param1, String param2) {
        NamesFragment fragment = new NamesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_name_frag, menu);
        this.menu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_play:
                if(playButtonStatus.equals("idle") || playButtonStatus.equals("stopped")) {
                    mPlayer = MediaPlayer.create(getContext(), R.raw.test);
                    playButtonStatus = "playing";
                    mPlayer.start();
                    menu.getItem(0).setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_lock_silent_mode));
                }else{
                    playButtonStatus = "stopped";
                    mPlayer.stop();
                    menu.getItem(0).setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_lock_silent_mode_off));
                }
                return true;
            case R.id.action_reminder:
                setReminder();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_name, container, false);
        mRecyclerView= view.findViewById(R.id.rv);
        scrollButton= view.findViewById(R.id.fabScroll);
        scrollButton.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fadein));
        nameList =new ArrayList<>();
        mPlayer = MediaPlayer.create(getContext(), R.raw.test);

        prefs = getActivity().getSharedPreferences("asmaulhusna", MODE_PRIVATE);
        editor = getActivity().getSharedPreferences("asmaulhusna", MODE_PRIVATE).edit();

        nameList.add("Ar Rahman#الرحمن#The Most Gracious#Who repeat this name 100 times after each fard (Obligatory) prayer will have a good memory, a keen awareness, and be free of a heavy heart");
        nameList.add("Ar Raheem#الرحيم#The Most Merciful#Who repeats this name 100 times after each Fajr prayer will find everyone to be friendly towards him and he will be safe from all worldly calamities.");
        nameList.add("Al Malik#الملك#The King#He who repeats this name many times every day after the Fajr prayer will become rich by the grace of Allah.");
        nameList.add("Al Quddus#القدوس#The Most Holy#Agitation never comes close if someone recites it 100 times every day.");
        nameList.add("As Salam#السلام#The Ultimate Provider of Peace#He, who repeats this name 160 times to a sick person, will help him to regain  He who repeats this name frequently will be safe from all calamities");
        nameList.add("Al Mu'min#المؤمن#The Guardian of Faith#Who repeats this name 631 times will be safe from harm.");
        nameList.add("Al Muhaymin#المهيمن#The Guardian, the Preserver#He who takes bath and offers two rakats of prayer and repeats this Name 100 times with sincere concentration, Allah will purify his external as well as internal condition.");
        nameList.add("Al Aziz#العزيز#The Almighty, the Self SufficientHe who repeats this Name 41 times after each Fard prayer will be independent of need from others and gain honor after disgrace.");
        nameList.add("Al Jabbaar#الجبار#The Compeller#He who repeats this Name will not be compelled to do anything against his wishes, and will not be exposed to violence, severity or hardness.");
        nameList.add("Al Mutakabbir#الْمُتَكَبِّرُ#The Dominant one#He who repeats this Name frequently will be granted status and respect. If He repeats this Name frequently at the commencement of every act, He will achieve success by the grace of Allah.");
        nameList.add("Al Khaaliq#الخالق#The Creator#Allah will assign an angel to guide the person righteously if he recites this name 100 times continuously for a prescribed limit of 7 days.");
        nameList.add("Al Baari#البارئ#The Maker#This name only signifies that Allah created all things in proportion.");
        nameList.add("Al Musawwir#المصور#The Fashioner of Forms#Recite this name 21 times and do a Damm on water. Continue this for 7 consecutive days. Use the water for breaking the fast. Insha’Allah, the women will soon be blessed with a child.");
        nameList.add("Al Ghaffaar#الغفار#The Ever-Forgiving#He who repeats this Name will be forgiven his sins.");
        nameList.add("Al Qahhaar#القهار#The All Subduer#The soul of him who repeats this Name will conquer the desires of the flesh, and his heart will be made free from the attractions of the world and gain inner peace. This Name also frees one from being wronged.");
        nameList.add("Al Wahhaab#الوهاب#The Bestower#Reciting it frequently will remove poverty. Reciting it 40 times in the last Sajadah of Chasht prayer will relieve the person from starvation.");
        nameList.add("Ar Razzaaq#الرزاق#The Ever-Providing, The Sustainer#He who repeats this Name will be provided with sustenance by Allah.");
        nameList.add("Al Fattaah#الفتاح#The Ultimate Judge, The Opener of All Portals, the Victory Giver#The heart of him who repeats this Name will be open, and he will be given victory.");
        nameList.add("Al Alim#العليم#The All-Knowing, the Omniscient#He who repeats this Name, his heart will become luminous, revealing divine light.");
        nameList.add("Al Qaabidh#القابض#The Restrainer, the Straightener#He who recite this name on 4 pieces of food (fruit, bread, etc.) and eats them for 40 days will be free from hunger.");
        nameList.add("Al Baasit#الباسط#The Expander, the Munificent#He who repeats this Name 10 times after Chasht prayer with open hands (palms up), then rubs his face with his hands, will be free of need from others.");
        nameList.add("Al Khaafidh#الخافض#The Abaser, The Demeanor#Those who fast three days, and on the fourth day repeat this Name 70 times in a gathering, Allah will free them from harm by their enemy. Allah will fulfill the need of one who repeats this Name 500 times daily.");
        nameList.add("Ar Raafi'#الرافع#The Exalter#He who repeats this Name 101 times day and night, Allah will make him higher, as far as honor, richness, and merit are concerned.");
        nameList.add("Al Mu'izz#المعز#The Giver of Honour#He who repeats this name 140 times after Maghrib prayer on Monday or Friday nights, Allah will make him dignified in the eyes of others. That person will fear no one but Allah.");
        nameList.add("Al Muzil#المذل#The Giver of Dishonor#He who repeats this Name 75 times will be free from harm by those who are jealous of him and wish to harm him. Allah will protect him.");
        nameList.add("Al Sami'#السميع#The All-Hearing#He who repeats this Name 500, 100 or 50 times without speaking to anyone on Thursday after the chasht prayer, Allah will bestow on him anything he desires.");
        nameList.add("Al Basir#البصير#The All-Seeing#He who repeats this Names 100 times after Friday afternoon prayer, Allah will give this person light in his sight and enlighten his heart.");
        nameList.add("Al Hakam#الحكم#The Judge, the Ultimate Arbiter#He who repeats this Name many times at night, many secrets will be revealed to him.");
        nameList.add("Al 'Adl#العدل#The Utterly Just#On Friday night or day, if you write this Name on a piece of bread and eat it, people will obey you.");
        nameList.add("Al Latif#اللطيف#The Kind#He who repeats this Name 133 times daily will have an increase in his sustenance and all his affairs will be settled to his satisfaction.");
        nameList.add("Al Khabir#الخبير#The All-Aware#If a man is a victim of selfish desires and bad habits he will be relieved of these if he repeats this Name regularly.");
        nameList.add("Al Halim#الحليم#The Forbearer, The Indulgent#He, who writes this Name on a piece of paper, washes it with water and sprinkles that water on anything that thing will become safe from loss and calamities.");
        nameList.add("Al-‘Adheem#العظيم#The Magnificent, the Infinite#Those who repeat this Name many times will be respected.");
        nameList.add("Al Ghafur#الغفور#The All-Forgiving# He who has a headache, fever and despondent, and continuously repeats this Name will be relieved of his ailment and will have Allah’s forgiveness.");
        nameList.add("Ash Shakur#الشكور#The Grateful#He who is afflicted with monitory troubles or with any other calamity and suffering, if he repeats this Name 41 times daily.");
        nameList.add("Al Ali#العلي#The Sublimely Exalted#He who repeats this Name regularly and keep it with him after writing it on a piece of paper will attain high rank, affluence, and success in his (lawful) desires.");
        nameList.add("Al Kabir#الكبير#The Great#He who repeats this Name 100 times each day will have esteem.");
        nameList.add("Al Hafidh#الحفيظ#The Preserver, The Protector#He who repeats this Name frequently and keeps it with him will be protected against calamities.");
        nameList.add("Al Muqit#المقيت#The Nourisher#If someone with a bad mannered child repeats this Name into a glass of water, and gives this water to the child to drink, the child will have good manners.");
        nameList.add("Al Hasib#الحسيب#The Reckoner#He who faces any problem should repeat this Name many times.");
        nameList.add("Al Jalil#الجليل#The Majestic#He who writes this Name on a piece of paper with musk and saffron and keeps it with him and repeats this Name frequently will attain honor and Status.");
        nameList.add("Al Karim#الكريم#The Bountiful, the Generous#He who repeats this Name many times at bedtime will have esteem in this world (and the Hereafter among the learned and righteous people).");
        nameList.add("Ar Raqib#الرقيب#The Watchful#Who repeats this Name seven times on himself, his family and property, all will be under Allah’s protection.");
        nameList.add("Al Mujib#المجيب#The Responsive, the Answerer#The appeal of him who repeats this Name will be answered.");
        nameList.add("Al Wasi'#الواسع#The Vast, the All Encompassing#If one who has difficulty in earning, repeats this Name frequently, will have good earnings.");
        nameList.add("Al Hakim#الحكيم#The Wise#He who repeats this Name continuously (from time to time) will not have difficulties in his work, and Allah will open to him the door of wisdom.");
        nameList.add("Al Wadud#الودود#The Loving, the Kind One#This name helps in resolving a disagreement between two people if one gives the other person food after reciting this name 1000 times in his food.");
        nameList.add("Al Majid#المجيد#The All Glorious#He who repeats this Name gains glory.");
        nameList.add("Al Ba'ith#الباعث#The Raiser of the Dead#He who repeats this Name gains the fear of Allah.");
        nameList.add("Ash Shaheed#الشهيد#The Witness#Recite this name 21 times with the hand placed on the head of the disobedient person. Insha’Allah, he will become obedient.");
        nameList.add("Al Haqq#الحق#The Truth, the Real#If one has something and repeats this Name, he will find what is lost.");
        nameList.add("Al Wakil#الوكيل#The Trustee, the Dependable#He who is afraid of drowning, being burnt in a fire, or any similar danger, and repeats this Name continuously (from time to time), will be under the protection of Allah.");
        nameList.add("Al Qawiyy#القوي#The Strong#He who cannot defeat his enemy, and repeats this Name with the intention of not being harmed, will be free from his enemy’s harm.");
        nameList.add("Al Mateen#المتين#The Firm, the Steadfast#If one has troubles and repeats this Name, his troubles will disappear.");
        nameList.add("Al Wali#الولي#The Protecting Friend, Patron, and Supporter#He who repeats this Name is likely to be a walyullah, the friend of Allah.");
        nameList.add("Al Hamidu#الحميد#The All Praise Worthy#He who repeats this Name will be loved and praised.");
        nameList.add("Al Muhsi#المحصي#The Accounter, The Numberer of All#He who is afraid of being questioned on the Judgment Day, and repeats this Name 100 times daily, will have ease and clement.");
        nameList.add("Al Mubdi#المبدئ#The Producer, Originator, and Initiator of all#If this name is repeated and breathed towards a pregnant woman who is afraid of aborting, she will be free of danger.");
        nameList.add("Al Mu'id#المعيد#The Reinstater Who Brings Back All#If this name is repeated 70 times for someone who is away from his family, that person will return safely in seven days.");
        nameList.add("Al Muhyi#المحيي#The Giver of Life#If a person has a heavy burden and repeats this name seven times each day, his burden will be taken away.");
        nameList.add("Al Mumit#المميت#The Bringer of Death, the Destroyer#This name is repeated to destroy one’s enemy.");
        nameList.add("Al Hayy#الحي#The Ever Living#He who repeats this Name will have a long life.");
        nameList.add("Al Qayyum#القيوم#The Self Subsisting Sustainer of All# He who repeats this Name will not fall into inadvertency.");
        nameList.add("Al Waajid#الواجد#The Perceiver, the Finder, the Unfailing#He who repeats this name will have the richness of heart.");
        nameList.add("Al Maajid#الماجد#The Illustrious, the Magnificent#He who repeats this Name in privacy and sincerely, his heart will be enlightened.");
        nameList.add("Al Waahid#الواحد#The One, the All Inclusive, the Indivisible#He who repeats this name 1000 times in privacy and in a quiet place will be free from fear and delusion.");
        nameList.add("Al Ahad#الاحد#The One, the Indivisible#He who repeats this name 1000 times will have certain secrets opened to him.");
        nameList.add("As Samad#الصمد#The Everlasting,The Eternal Refuge#He who repeats this name many times, Allah will provide his need, and as result, he will not need others, but they will need him.");
        nameList.add("Al Qaadir#القادر#The All-Capable, The Most Able, The Most Powerful#He who repeats this name, all his desires will be fulfilled.");
        nameList.add("Al Muqtadir#المقتدر#The All Determiner, the Dominant#He who repeats this name will be aware of the truth.");
        nameList.add("Al Muqaddim#المقدم#The Expediter, He who brings forward#He who repeats this name on the battlefield, or who has fear of being alone in an awe-inspiring place, no harm will come to him and will become obedient to Allah.");
        nameList.add("Al Mu'akhkhir#المؤخر#The Delayer, He who brings backwards#He who repeats this name in the heart 100 times each day, only love of Allah will remain. No other love can enter.");
        nameList.add("Al Awwal#الأول#The First#A childless person will be blessed with a child if he repeats this name 40 times daily for 40 days. If a traveler repeats it 1000 times on a Friday he will reach home safe and sound.");
        nameList.add("Al Aakhir#الآخر#The Last# He who repeats this name many times will lead a good life and at the end of this life will have a good death.");
        nameList.add("Az Dhaahir#الظاهر#The Manifest; the All Victorious#He who recites this name 15 times after Friday (Jumma prayer) divine light will enter his heart.");
        nameList.add("Al Baatin#الباطن#The Hidden; the All Encompassing#He who repeats this name three times each day will be able to see the truth in things.");
        nameList.add("Al Waali#الوالي#The Patron#He who repeats this name and breathes it into his house, his house will be free from danger.");
        nameList.add("Al Muta'ali#المتعالي#The Self Exalted#He who repeats this name many times will gain the benevolence of Allah.");
        nameList.add("Al Barr#البر#The Most Kind and Righteous#He who repeats this name to his child, this child will be free from misfortune.");
        nameList.add("At Tawwaab#التواب#The Ever-Pardoning, Ever Relenting#He who repeats this name many times, his repentance will be accepted.");
        nameList.add("Al Muntaqim#المنتقم#The Avenger#He who repeats this name for 3 Fridays many times will be victorious against his enemies.");
        nameList.add("Al 'Afuww#العفو#The Pardoner, The Forgiver#He who repeats this name many times, all his sins will be forgiven.");
        nameList.add("Ar Ra'uf#الرؤوف#The Clement, The Compassionate, The All-Pitying#He who repeats this name many times will be blessed by Allah.");
        nameList.add("Malik Al Mulk#مالك الملك#The Owner of All Sovereignty#He who repeats this name will have esteem among people.");
        nameList.add("Dhual Jalal wa Al Ikram#ذو الجلال و الإكرام#The Lord of Majesty and Generosity#He, who repeats this name many times, will be rich.");
        nameList.add("Al Muqsit#المقسط#The Equitable, the Requiter#He who repeats this name will be free from the harm of the devil.");
        nameList.add("Al Jaami'#الجامع#The Gatherer, the Unifier#He who repeats this name will find the things that he lost.");
        nameList.add("Al Ghani#الغني#The All Rich, the Independent#He who repeats this name 70 times will be contented and not covetous and will not be needy.");
        nameList.add("Al Mughni#المغني#The Enricher, the Emancipator#He who repeats this name 1000 times daily for ten Fridays will become self-sufficient.");
        nameList.add("Al Mani'#المانع#The Withholder, the Shielder, the Defender#One should repeat this name 20 times at bedtimes for a peaceful and happy family life.");
        nameList.add("Ad Dharr#الضآر#The Distresser#He who does not enjoy peace and tranquility in life should repeat this name 100 times on Friday nights. He will find peace and tranquility by the grace of Allah.");
        nameList.add("An Nafi'#النافع#The Propitious, the Benefactor#He, who repeats this name 41 times at the beginning of every act, will be successful in all his good acts.");
        nameList.add("An Nur#النور#The Light#Those who repeat this name will have an inner light.");
        nameList.add("Al Hadi#الهادي#The Guide#Ho who repeats 1100 times the sacred names “yahadi ihdinas-siratal-mustaqim” after the Isha prayer will be free from all needs.");
        nameList.add("Al Badi'i#البديع#Incomparable, the Originator#He who is confronted by any distress or difficulty should repeat this name 70,000 times for relief from the distress.");
        nameList.add("Al Baaqi#الباقي#The Ever Enduring and Immutable#He who repeats this name every Friday night 100 times all his good deeds will be accepted, by the grace of Allah.");
        nameList.add("Al Waarith#الوارث#The Heir, the Inheritor of All# He, who repeats this name after sunrise 100 times, will be safe from all sorrows, by the grace of Allah.");
        nameList.add("Ar Rashid#الرشيد#The Guide, Infallible Teacher, and Knower#He who does not have the know-how about a particular task or unable to work out plans for a certain task should repeat this name 1000 times between Maghrib and Isha Prayer.");
        nameList.add("Al Saboor#الصبور#The Forbearing, The Patient#He, who is in any trouble, difficulty or sorrow and repeat this name 3000 times, will be rescued from his difficulty.");


//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setLayoutManager(new CustomLinearLayoutManager(getContext()));

        namesAdapter=new NameAdapter(getContext(), nameList);

        mRecyclerView.setAdapter(namesAdapter);

        scrollButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(scrollButtonStatus.equals("idle") || scrollButtonStatus.equals("pause")){
                    scrollButtonStatus = "playing";
                    scrollButton.setFabIcon(R.drawable.ic_media_pause);
                    scrollButton.setFabText("Pause");
                    mRecyclerView.smoothScrollToPosition(position);
                    position++;
                    T=new Timer();
                    T.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            if(position <= 99 && scrollButtonStatus.equals("playing")) {
                                mRecyclerView.smoothScrollToPosition(position);
                                position++;
                            }else {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        // this will run in the main thread
                                        scrollButtonStatus = "pause";
                                        scrollButton.setFabIcon(R.drawable.ic_media_play);
                                        scrollButton.setFabText("Scroll");
                                    }
                                });

                            }
                        }
                    }, 0, 1000);
                }else if(scrollButtonStatus.equals("playing")){
                    stopScrolling();
                }

            }
        });

        return view;
    }

    private void stopScrolling() {
        if(T != null) {
            T.cancel();
            T.purge();
        }
        scrollButtonStatus = "pause";
        scrollButton.setFabIcon(R.drawable.ic_media_play);
        scrollButton.setFabText("Scroll");
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    private void setReminder() {
        stopScrolling();
        final int hour,minute;
        Calendar mcurrentTime = Calendar.getInstance();
        if(prefs.getInt("asma_hour", 25) != 25){
            hour = prefs.getInt("asma_hour", 25);
            minute = prefs.getInt("asma_minute", 25);
        }else{
            hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            minute = mcurrentTime.get(Calendar.MINUTE);
        }

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                calendar.set(Calendar.MINUTE, selectedMinute);
                calendar.set(Calendar.SECOND, 0);
                Intent intent1 = new Intent(getActivity(), AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager am = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);
                am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                editor.putInt("asma_hour", selectedHour);
                editor.putInt("asma_minute", selectedMinute);
                editor.commit();
                Toast.makeText(getActivity(),"Reminder set successfully!",Toast.LENGTH_LONG).show();
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        stopScrolling();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

class CustomLinearLayoutManager extends LinearLayoutManager{
    public CustomLinearLayoutManager(Context context) {
        super(context);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        final LinearSmoothScroller linearSmoothScroller =
                new LinearSmoothScroller(recyclerView.getContext()) {
                    private static final float MILLISECONDS_PER_INCH = 100f;

                    @Override
                    public PointF computeScrollVectorForPosition(int targetPosition) {
                        return CustomLinearLayoutManager.this
                                .computeScrollVectorForPosition(targetPosition);
                    }

                    @Override
                    protected float calculateSpeedPerPixel
                            (DisplayMetrics displayMetrics) {
                        return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
                    }
                };
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }
}
