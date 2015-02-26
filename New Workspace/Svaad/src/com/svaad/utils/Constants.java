package com.svaad.utils;

import java.util.ArrayList;
import java.util.List;

public class Constants {

	public static final int CAPTURE_IMAGE = 100;
	public static final int GALLERY_IMAGE = 200;
	public static final String APPLICATION_ID = "X-Parse-Application-Id";
	public static final String APPLICATION_ID_VALUE = "TsgRw9ddL9MqSSMDfJZjepbdoqwfZeSMIyE6FN2U";
	public static final String REST_API_KEY = "X-Parse-REST-API-Key";
	public static final String REST_API_VALUE = "85ZnxBuGlJsbd3QohAUrsP0uC8xDd09usirj9786";
	public static final String X_PARSE_SESSION_TOKEN = "X-Parse-Session-Token";
	public static final String USER_PROFILE_SCREEN = "USER_PROFILE_SCREEN";
	public static final String TEMPPIC="http://files.parse.com/51c0044d-5d30-415f-bdb6-70e674936a2e/67ccf2ce-aeb2-4305-be0f-bcc359233814-cuadros.png";
	public static final String TEMP_PIC="http://files.parse.com/51c0044d-5d30-415f-bdb6-70e674936a2e/41445399-cf7d-4667-8bfe-f46dac516737-temp.png";

	public static final String SESSION_TOCKEN_KEY = "SESSION_TOCKEN_KEY";
	public static final String IMAGE_PATH = "IMAGE_PATH";
	public static final String DATA = "data";
	public static final String DATA_mostloved = "mostloved";
	public static final String DATA_Res = "data_res";
	public static final String Cam_ate = "cam_ate";
	public static final String ADD_DISH = "adddish";
	public static final String QUERYTEXT="queryText";
	public static final String SEE_MORE_DISHES="dishes";
	public static final String SEE_MORE_RES="res";
	
	public static final String MIXPANEL_API_TOKEN = "44bdd68c4bb8d2ff6f4d3d20044ac096";
	public static final String USER_NAME_MIXPANEL="userloginname";
	
	public static final int PAGER_MODE_FEED = 1;
	public static final int PAGER_MODE_ME = 2;
	public static final int RES_MODE = 100;
	public static final String PAGER = "pager_mode";
	
	public static final String SPLASH = "splash_screen";
	
	public static final int DISH_DETAIL_RESULT = 300;
	public static final String LOG_IN_SCREEN = "LOG_IN_SCREEN";
	public static final String SIGN_UP_SCREEN = "SIGN_UP_SCREEN";
	public static final String LOG_IN_FB_SCREEN = "LOG_IN_FB_SCREEN";
	public static final String SIGN_UP_FB_SCREEN = "SIGN_UP_FB_SCREEN";
	public static final String USER_OBJECT_ID_KEY = "USER_OBJECT_ID_KEY";
	public static final String USER_OBJECT_INFO = "USER_OBJECT_INFO";
	public static final String USER_NAME = "USER_NAME";
	public static final String PROFILE_PIC_URL = "PROFILE_PIC_URL";
	public static final String DISH_DETAIL_JSON = "DISH_DETAIL_JSON";
	public static final String FOLLOWING_USERS = "FOLLOWING_USERS";
//	public static final String FOLLOWERS_ARRAY = "FOLLOWERS_ARRAY";
	public static final String WISHLIST_ARRAY = "WISHLISY_ARRAY";
	public static final String LIKES_LIST = "LIKES_LIST";
	
	public static final String CAM_DISHES_DATA = "CAM_DISHES_DATA";
	public static final String CAM_RES_DATA = "CAM_RES_DATA";
	
	
	public static final String LOVED_IT="lovedit";
	public static final String GOOD="good";
	public static final String ITS_OK="itsok";
	public static final String YUCK="yuck";
	public static final String HEIGHT = "HEIGHT";
	public static final String WIDTH = "WIDTH";
	public static final String SVAADLOGIN_RESPONSE="SVAADLOGIN_RESPONSE";
	public static final String className = "_User";

	public static final String ALL_LOCATIONS_URL = "https://api.parse.com/1/classes/Location";
	public static final String LOCATIONS_DISHES_URL = "https://api.parse.com/1/functions/getLocationDishes";
	public static final String SIGN_UP_URL = "https://api.parse.com/1/users";
	public static final String LOG_IN_URL = "https://api.parse.com/1/login";
	public static final String FEED_URL="https://api.parse.com/1/classes/DishComments";	
	public static final String FEED_URL_IMAGEUPLOADING="https://api.parse.com/1/classes/DishCommentsNew";
	public static final String DISH_COMMENTS_URL="https://api.parse.com/1/classes/DishCommentsNew";
	
	public static final String USER_FOLLW = "https://api.parse.com/1/functions/userFollow";
	public static final String USER_UNFOLLW = "https://api.parse.com/1/functions/userUnFollow";
	
	public static final String CAM_ATE_IT = "https://api.parse.com/1/functions/ateit";
	public static final String CAM_DISH_ATE_IT = "https://api.parse.com/1/functions/createAteit";
	
	public static final String NEW_LIKE_URl="https://api.parse.com/1/functions/addWishlist";
	
	public static final String UPLOAD_IMAGE_URL = "https://api.parse.com/1/files/";
	public static final String SAVE_PHOTO_URL = "https://api.parse.com/1/classes/DishCommentsNew";
	public static final String UNLiKE_URL = "https://api.parse.com/1/functions/removeWishlist";	
	public static final String LIKE_URL = "https://api.parse.com/1/classes/ListDishes";
	public static final String POPULAR_RESTAURANT_URL = "https://api.parse.com/1/classes/Branches";
	public static final String NEAREST_DISHES_URL = "https://api.parse.com/1/functions/getNearestDishes";
	public static final String BRANCH_MENU_URL = "https://api.parse.com/1/classes/BranchDish";
	public static final String HOME_URL = "https://api.parse.com/1/classes/HomeScreen";
	
	public static final String ALL_FOODIES_URL = "https://api.parse.com/1/users";
	
	public static final String BRANCH_DETAILS="branch_details";
	//Mixpanel Constants
	public static final String DISH_PROFILE="Dishprofile";
	public static final String SVAAD="Svaad";
	public static final String SEARCH="Search";
	public static final String ME="Me";
	public static final String USER_PROFILE="UserProfile";
	public static final String WISHLIST="Wishlist";
	public static final String RESTAURANT_MENU="Restaurant Menu";
	public static final String DISH_COLLECTION="Dish Collection";
	public static final String ATE_IT="Ate it";
	public static final String LOGIN_EMAIL="LOGIN_WITH_EMAIL";
	public static final String LOGIN_FB="LOGIN_FB";
	public static final String WISH_IT="WIsh it";
	public static final String WISHED="WIshed";
	public static final String FOLLOWING_ACTION="Following";
	public static final String FOLLOW_ACTION="Follow";
	public static final String SVAAD_LOGIN="Svaad_Login";
	public static final String SVAAD_JOIN="Svaad_Join";	
	public static final String ME_LOGIN="Me_Login";
	public static final String Me_JOIN="Me_Join";
	public static final String FOODIES="FOODIES";
	public static final String LOGIN_OBJECTID="Login objectid";
	public static final String NAME="name";
	public static final String EMAIL="email";
	public static final String SCREEN="screen";
	public static final String ACTION="action";
	
	
	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}

	public static List<String[]> getPriceRange() {
		List<String[]> priceRange = new ArrayList<String[]>();

		int min = 0;
		int max = 50;

		for (int i = 0; i < 6; i++) {
			String[] strings = { "" + min, "" + max };
			min = max;
			max += 50;
			priceRange.add(strings);
		}
		String[] strings = { "" + min, "" + 500 };
		String[] strings1 = { "" + 500, "" + 1000 };
		String[] strings2 = { "" + 1000 };
		priceRange.add(strings);
		priceRange.add(strings1);
		priceRange.add(strings2);

		return priceRange;

	}
	
	public static final String DISH_CATEGORY_TYPE_EVERYONE="Everyone";
	public static final String DISH_CATEGORY_TYPE_FEED="Feed";
	
	public static final String DISH_LOCATION_SPINNER_NEARBY="Near by";
	public static final String DISH_LOCATION_SPINNER_ALL_LOCATION="All Locations";
	
	public static final String DISH_CATEGORY_TAG="CategoryTag";
	public static final String DISH_LOCATION_TAG="LocationTag";

	
	
	public static final String EXPLORE_NEAR_BY="Explore nearby";
	public static final String EXPLORE_HYDERABAD="Explore Hyderabad";
	
	public static final String FOLLOWING="Following";
	public static final String FOLLOWING_NEAR_BY="Following nearby";
	
}


