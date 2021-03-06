package com.felipecsl.asymmetricgridview.app;

import android.util.Log;

import com.felipecsl.asymmetricgridview.app.model.DemoItem;
import com.felipecsl.asymmetricgridview.app.presenter.ApiObject;

import java.util.ArrayList;
import java.util.List;

final class DemoUtils {
  int currentOffset;

  DemoUtils() {
  }

  public List<DemoItem> moreItems(int qty, List<ApiObject> postList) {
	List<DemoItem> items = new ArrayList<>();
	boolean isLeft = true;
	int count = 0;

	for (int i = 0; i < qty; i++) {
	    count++;
		int colSpan = 1;
	  if(i == 0 && isLeft) {
		  colSpan = 2;
		  isLeft = !isLeft;
		  count = 0;
	  } else if(!isLeft && count == 10) {
          colSpan = 2;
          isLeft = !isLeft;
          count = 0;
      } else if(isLeft && count == 8) {
          colSpan = 2;
          isLeft = !isLeft;
          count = 0;
      }
	  int rowSpan = colSpan;
	  String url;
	  if(rowSpan == 2) {
	      url = postList.get(i).getUrl();
      } else {
	      url = postList.get(i).getUrl();
      }
      url = url.replace("\\/", "/");
      Log.d("retro", "i: " + i + " url: " +url + " isVideo: " + (rowSpan == 2));
	  DemoItem item = new DemoItem(colSpan, rowSpan, currentOffset + i, url);
	  items.add(item);
	}

	currentOffset += qty;

	return items;
  }


    public List<DemoItem> moreItems(int qty) {
        List<DemoItem> items = new ArrayList<>();
        boolean isLeft = true;
        int count = 0;

        for (int i = 0; i < qty; i++) {
            count++;
            int colSpan = 1;
            if(i == 0 && isLeft) {
                colSpan = 2;
                isLeft = !isLeft;
                count = 0;
            } else if(!isLeft && count == 10) {
                colSpan = 2;
                isLeft = !isLeft;
                count = 0;
            } else if(isLeft && count == 8) {
                colSpan = 2;
                isLeft = !isLeft;
                count = 0;
            }
            int rowSpan = colSpan;
            String url;
            if(rowSpan == 2) {
                url = "https://sample-videos.com/video123/mp4/240/big_buck_bunny_240p_30mb.mp4";
            } else {
                url = "https://b.zmtcdn.com///data///reviews_photos///2f3///4c5968aa388e753f5c2fff26eb8962f3_1545998762.jpg";
            }
            DemoItem item = new DemoItem(colSpan, rowSpan, currentOffset + i, url);
            items.add(item);
        }

        currentOffset += qty;

        return items;
    }
}
