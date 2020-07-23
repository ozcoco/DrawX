package org.oz.draw.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import org.oz.draw.R;
import org.oz.draw.view.DrawView;
import org.oz.draw.view.LineUtils;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String TAG = PlaceholderFragment.class.getName();

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }


    int index = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);

        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        if (index != 1) {

            View root = inflater.inflate(R.layout.fragment_main, container, false);
            final TextView textView = root.findViewById(R.id.section_label);
            pageViewModel.getText().observe(this, new Observer<String>() {
                @Override
                public void onChanged(@Nullable String s) {
                    textView.setText(s);
                }
            });

            return root;

        } else {

            return inflater.inflate(R.layout.sample_draw_view, container, false);
        }

    }


    private LineUtils.CubicBezier cubicBezier;
    private LineUtils.QuadBezier quadBezier;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final AppCompatSeekBar seeker = view.findViewById(R.id.seeker);
        final AppCompatSeekBar seeker2 = view.findViewById(R.id.seeker2);
        final DrawView drawer = view.findViewById(R.id.drawer);

        drawer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                drawer.setAd(drawer.getX0(),
                        drawer.getY0());

                return false;
            }
        });

        seeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (fromUser) {

                    seeker.setMax(drawer.getWp() - 100);

                    cubicBezier = new LineUtils.CubicBezier(
                            drawer.getX0(),
                            drawer.getY0(),
                            drawer.getX1(),
                            drawer.getY1(),
                            drawer.getX2(),
                            drawer.getY2(),
                            drawer.getX3(),
                            drawer.getY3());

                    cubicBezier.setMax(drawer.getWp());

                    float x = drawer.getX0() + progress;

                    float y = cubicBezier.y(x);

                    Log.d(TAG, "-------->(" + x + "," + y + ")");

                    if (y > 0)
                        drawer.setAd(x, y);

                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        seeker2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (fromUser) {

                    seeker2.setMax(drawer.getWp() - 100);

                    quadBezier = new LineUtils.QuadBezier(
                            drawer.getQx0(),
                            drawer.getQy0(),
                            drawer.getQx1(),
                            drawer.getQy1(),
                            drawer.getQx2(),
                            drawer.getQy2()
                    );

                    quadBezier.setMax(drawer.getWp());

                    float x = drawer.getQx2() + progress;

                    float y = quadBezier.findYByX(x);

                    Log.d(TAG, "------quadBezier-->(" + x + "," + y + ")");

                    if (y > 0)
                        drawer.setQad(x, y);

                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}