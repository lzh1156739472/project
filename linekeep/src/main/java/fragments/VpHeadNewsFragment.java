package fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.linekeep.pangu.linekeep.R;
import bean.HomeHotBean;

/**
 * Created by Administrator on 2017/8/7.
 */

public class VpHeadNewsFragment extends Fragment {
    private ImageView mImageView;
    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_vp_head_news, null);
        initView(view);
        Bundle bundle = getArguments();

        HomeHotBean homeHotBean = (HomeHotBean) bundle.getSerializable("duixiang");
        List<HomeHotBean.ArticlesBean.NewsItemsBean> listNewsItem = homeHotBean.getArticles().get(0).getNewsItems();
        int id = bundle.getInt("idKey");
        int newType = 0;
        int newItemsCount = listNewsItem.size();
        if (id == 0) {
            newType = listNewsItem.get(newItemsCount - 1).getNewsType();
        }
        if (id <= listNewsItem.size() && id > 0) {
            newType = listNewsItem.get(id - 1).getNewsType();
        }
        if (id > listNewsItem.size()) {
            newType = listNewsItem.get(0).getNewsType();
        }
        Log.e("TAG6", newType + "");
        switch (id) {
            case 0://id  传过来的 0 1 2 3 4 5 6 7  对应碎片数，但是，要一个和newItmes的item数，里面有-1处理，预防越界，或者错乱
                if (newType == 3 || newType == 4||newType==8) {
                Glide.with(getContext()).load(listNewsItem.get(listNewsItem.size() - 1).getPics().get(0)).into(mImageView);
                textView.setText(listNewsItem.get(listNewsItem.size() - 1).getTitle());
            } else if (newType == 21) {

                Glide.with(getContext()).load(listNewsItem.get(listNewsItem.size() - 1).getData().getResource().getFile()).into(mImageView);
                textView.setText(listNewsItem.get(listNewsItem.size() - 1).getData().getResource2().getText());
            }
                break;
            case 1:
                if (newType == 3 || newType == 4||newType==8) {
                    Glide.with(getContext()).load(listNewsItem.get(id - 1).getPics().get(0)).into(mImageView);
                    textView.setText(listNewsItem.get(id - 1).getTitle());
                } else if (newType == 21) {
                    Glide.with(getContext()).load(listNewsItem.get(id - 1).getData().getResource().getFile()).into(mImageView);
                    textView.setText(listNewsItem.get(id - 1).getData().getResource2().getText());
                }
                break;
            case 2:
                if (newType == 3 || newType == 4||newType==8) {
                    Glide.with(getContext()).load(listNewsItem.get(id - 1).getPics().get(0)).into(mImageView);
                    textView.setText(listNewsItem.get(id - 1).getTitle());
                } else if (newType == 21) {
                    Glide.with(getContext()).load(listNewsItem.get(id - 1).getData().getResource().getFile()).into(mImageView);
                    if (listNewsItem.get(id - 1).getData().getResource2().getText() != null) {
                        textView.setText(listNewsItem.get(id - 1).getData().getResource2().getText());
                    }
                }
                break;

            case 3:
                if (newType == 3 || newType == 4||newType==8) {
                    Glide.with(getContext()).load(listNewsItem.get(id - 1).getPics().get(0)).into(mImageView);
                    textView.setText(listNewsItem.get(id - 1).getTitle());
                } else if (newType == 21||newType==12) {
                    Glide.with(getContext()).load(listNewsItem.get(id - 1).getData().getResource().getFile()).into(mImageView);
//                    if (listNewsItem.get(id - 1).getData().getResource2().getText() != null) {
//                        textView.setText(listNewsItem.get(id - 1).getData().getResource2().getText());
//                    }
                }
                break;

            case 4:
                if (newType == 3 || newType == 4||newType==8) {
                    Glide.with(getContext()).load(listNewsItem.get(id - 1).getPics().get(0)).into(mImageView);
                    textView.setText(listNewsItem.get(id - 1).getTitle());
                } else if (newType == 21) {
                    Glide.with(getContext()).load(listNewsItem.get(id - 1).getData().getResource().getFile()).into(mImageView);
                    if (listNewsItem.get(id - 1).getData().getResource2().getText() != null) {
                        textView.setText(listNewsItem.get(id - 1).getData().getResource2().getText());
                    }
                }
                break;
            case 5:
                if (newType == 3 || newType == 4||newType==8) {
                    Glide.with(getContext()).load(listNewsItem.get(id - 1).getPics().get(0)).into(mImageView);
                    textView.setText(listNewsItem.get(id - 1).getTitle());
                } else if (newType == 21) {
                    Glide.with(getContext()).load(listNewsItem.get(id - 1).getData().getResource().getFile()).into(mImageView);
                    if (listNewsItem.get(id - 1).getData().getResource2().getText() != null) {
                        textView.setText(listNewsItem.get(id - 1).getData().getResource2().getText());
                    }
                }
                break;
            case 6:
                if (newType == 3 || newType == 4||newType==8) {
                    if (newItemsCount == 5) {
                        Glide.with(getContext()).load(listNewsItem.get(5 - 1).getPics().get(0)).into(mImageView);
                        textView.setText(listNewsItem.get(5 - 1).getTitle());
                    } else {
                        Glide.with(getContext()).load(listNewsItem.get(id - 1).getPics().get(0)).into(mImageView);
                        textView.setText(listNewsItem.get(id - 1).getTitle());
                    }

                } else if (newType == 21) {
                    Glide.with(getContext()).load(listNewsItem.get(id - 1).getData().getResource().getFile()).into(mImageView);
                    if (listNewsItem.get(id - 1).getData().getResource2().getText() != null) {
                        textView.setText(listNewsItem.get(id - 1).getData().getResource2().getText());
                    }
                }
                break;
            case 7:
                if (newType == 3 || newType == 4||newType==8) {
                    Glide.with(getContext()).load(listNewsItem.get(0).getPics().get(0)).into(mImageView);
                    textView.setText(listNewsItem.get(0).getTitle());
                } else if (newType == 21) {
                    Glide.with(getContext()).load(listNewsItem.get(0).getData().getResource().getFile()).into(mImageView);
                    if (listNewsItem.get(id - 1).getData().getResource2().getText() != null) {
                        textView.setText(listNewsItem.get(id - 1).getData().getResource2().getText());
                    }
                }
                break;
            default:
                break;

        }

        return view;
    }

    private void initView(View view) {
        mImageView = (ImageView) view.findViewById(R.id.iv_vp_head_news);
        textView = (TextView) view.findViewById(R.id.tv_vphead_news_title);
    }
}
