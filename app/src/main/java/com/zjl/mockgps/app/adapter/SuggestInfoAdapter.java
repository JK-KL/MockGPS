package com.zjl.mockgps.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.route.SuggestAddrInfo;
import com.zjl.mockgps.app.MapActivity;
import com.zjl.mockgps.app.R;

import java.util.List;


/**
 * Created by C0dEr on 15/8/18.
 */
public class SuggestInfoAdapter extends BaseExpandableListAdapter {
    private final int START = 0;
    private final int END = 1;
    private final int MIDWAY = 2;

    private Context mContext;
    private SuggestAddrInfo addrInfo;

    private List<PoiInfo> groupStart;
    private List<PoiInfo> groupEnd;
    private List<List<PoiInfo>> groupMid;

    public SuggestInfoAdapter(Context mContext, SuggestAddrInfo addrInfo) {
        this.mContext = mContext;
        this.addrInfo = addrInfo;
        groupStart = addrInfo.getSuggestStartNode();
        groupEnd = addrInfo.getSuggestEndNode();
        groupMid = addrInfo.getSuggestWpNode();
    }


    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getGroupCount() {
        //TODO 这里暂时不考虑中途点的问题

        return 2;
    }

    @Override
    public int getChildrenCount(int i) {
        switch (i) {
            case START:
                return addrInfo.getSuggestStartNode() == null ? 0 : addrInfo.getSuggestStartNode().size();
            case END:
                return addrInfo.getSuggestEndNode() == null ? 0 : addrInfo.getSuggestEndNode().size();
            case MIDWAY:
                return addrInfo.getSuggestWpNode() == null ? 0 : addrInfo.getSuggestWpNode().size();
            default:
                return 0;
        }
    }

    @Override
    public Object getGroup(int i) {
        switch (i) {
            case START:
                return addrInfo.getSuggestStartNode();
            case END:
                return addrInfo.getSuggestEndNode();
            case MIDWAY:
                return addrInfo.getSuggestWpNode();
            default:
                return null;
        }
    }

    @Override
    public Object getChild(int i, int i1) {
        switch (i) {
            case START:
                return addrInfo.getSuggestStartNode().get(i1);
            case END:
                return addrInfo.getSuggestEndNode().get(i1);
            case MIDWAY:
                return addrInfo.getSuggestWpNode().get(i1);
            default:
                return null;
        }
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        ViewHolder mViewHolder = null;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.fragment_suggest_parent, null);
            mViewHolder = new ViewHolder();
            view.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) view.getTag();
        }
        mViewHolder.groupName = (TextView) view.findViewById(R.id.groupName);
        switch (i) {
            case START:
                mViewHolder.groupName.setText("请确定起始点");
                break;
            case END:
                mViewHolder.groupName.setText("请确定结束点");
                break;
            case MIDWAY:
                mViewHolder.groupName.setText("请确定中途点");
                break;
        }
        return view;
    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {

        ViewHolder mViewHolder = null;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.fragment_suggest_list, null);
            mViewHolder = new ViewHolder();
            view.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) view.getTag();
        }
        if (i == 3) {
            return null;
        }
        mViewHolder.option = (Button) view.findViewById(R.id.option);
        mViewHolder.option.setText(((PoiInfo) getChild(i, i1)).address + ((PoiInfo) getChild(i, i1)).name);
        mViewHolder.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (i) {
                    case START:
                        ((MapActivity) mContext).startPoint.setText(((PoiInfo) getChild(i, i1)).name);
                        break;
                    case END:
                        ((MapActivity) mContext).endPoint.setText(((PoiInfo) getChild(i, i1)).name);
                        break;
                    case MIDWAY:
                        break;
                }
            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return (addrInfo.getSuggestEndNode().size() == 0 && addrInfo.getSuggestStartNode().size() == 0 && addrInfo.getSuggestWpNode().size() == 0) ? true : false;
    }

    @Override
    public void onGroupExpanded(int i) {

    }

    @Override
    public void onGroupCollapsed(int i) {

    }

    @Override
    public long getCombinedChildId(long l, long l1) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long l) {
        return 0;
    }

    class ViewHolder {
        public Button option;
        public TextView groupName;
    }
}