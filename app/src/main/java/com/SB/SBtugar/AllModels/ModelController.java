package com.SB.SBtugar.AllModels;

import android.content.Context;
import android.util.Log;
import com.SB.SBtugar.AllModels.Orders.OrderResponse;
import com.SB.SBtugar.AllModels.SuperMarket.SuperMarket;
import com.SB.SBtugar.AllModels.SuperMarket.SuperMarketsResponse;
import com.SB.SBtugar.AllModels.listener.IAreaListObserver;
import com.SB.SBtugar.AllModels.listener.IMarketCategoriesObserver;
import com.SB.SBtugar.AllModels.listener.IMarketDataObserver;
import com.SB.SBtugar.AllModels.listener.IMarketsListObserver;
import com.SB.SBtugar.AllModels.listener.IOrderListObserver;
import com.SB.SBtugar.AllModels.listener.IOrderSubmitObserver;
import com.SB.SBtugar.AllModels.listener.IProductListObserver;
import com.SB.SBtugar.AllModels.listener.IUserDataObserver;
import com.SB.SBtugar.AllModels.listener.IUserDataUpdateObserver;
import com.SB.SBtugar.CartData.AppDatabase;
import com.SB.SBtugar.SeverData.Constant;
import com.SB.SBtugar.SeverData.ServiceGenerator;
import com.SB.SBtugar.SeverData.StoreAPIInterface;
import com.SB.SBtugar.dataGrabber.OffersGrabber;
import com.SB.SBtugar.dataGrabber.OrderSubmitter;
import com.SB.SBtugar.dataGrabber.OrdersGrabber;
import com.SB.SBtugar.dataGrabber.UserDataGrabber;
import com.SB.SBtugar.dataGrabber.UserUpdateSubmitter;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import androidx.annotation.NonNull;
import androidx.room.Room;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModelController {

    private Model model;
    private static ModelController modelController;

    private List<IMarketsListObserver> marketListObservers;
    private List<IMarketDataObserver> marketDataObservers;
    private List<IMarketCategoriesObserver> marketCategoriesObservers;
    private List<IAreaListObserver> areaListObservers;
    private List<IProductListObserver> productListObservers;
    private List<IUserDataObserver> userDataObservers;
    private List<IOrderSubmitObserver> orderSubmitObservers;
    private List<IOrderListObserver> orderListObservers;
    private List<IUserDataUpdateObserver> userDataUpdateObservers;

    private ModelController() {
        model = Model.getInstance();
        marketListObservers = new ArrayList<>();
        marketDataObservers = new ArrayList<>();
        marketCategoriesObservers = new ArrayList<>();
        areaListObservers = new ArrayList<>();
        productListObservers = new ArrayList<>();
        userDataObservers = new ArrayList<>();
        orderSubmitObservers = new ArrayList<>();
        orderListObservers = new ArrayList<>();
        userDataUpdateObservers = new ArrayList<>();
    }

    public static synchronized ModelController getInstance() {
        if (modelController == null) {
            modelController = new ModelController();
        }
        return modelController;
    }
    public void SubscribeToIt(String title){
        FirebaseMessaging.getInstance().subscribeToTopic(title);
    }
    public void unSubscribeFromIt(String title){
        FirebaseMessaging.getInstance().unsubscribeFromTopic(title);
    }
    public Model getModel() {
        return model;
    }

    public void requestSupportedAreas() {
        StoreAPIInterface storeServices = ServiceGenerator.createService(
                StoreAPIInterface.class, Constant.BASE_URL_V2);
        storeServices.getAreas(Locale.getDefault().getLanguage()).enqueue(new Callback<AreaResponse>() {
            @Override
            public void onResponse(@NonNull Call<AreaResponse> call, @NonNull Response<AreaResponse> response) {
                Log.e("DataIs", "success to get Areas");
                AreaResponse areaResponse = response.body();
                model.setAreas(areaResponse);
                notifyAreaListObservers();
            }

            @Override
            public void onFailure(@NonNull Call<AreaResponse> call, @NonNull Throwable t) {
                Log.e("DataIs", "Failed to get Areas", t);
            }
        });
    }


    public void requestMarketList( String catCode,Context context) {
        StoreAPIInterface storeServices = ServiceGenerator.createService(
                StoreAPIInterface.class, Constant.BASE_URL_V2);
        Log.e("requestMarketList",catCode);
        storeServices.getSuperMarkets(catCode,"All Dists.","Port said-بورسعيد")
                .enqueue(new Callback<SuperMarketsResponse>() {
            @Override
            public void onResponse(@NonNull Call<SuperMarketsResponse> call,
                                   @NonNull Response<SuperMarketsResponse> response) {
                if (response.body() == null || response.body().getData() ==null)
                    return;
                List<SuperMarket> superMarketList = response.body().getData().getAllstores();
                model.setMarketList(superMarketList);
                notifyMarketListObservers(superMarketList);
            }

            @Override
            public void onFailure(@NonNull Call<SuperMarketsResponse> call, Throwable t) {
            }
        });
    }




    public void requestMarketInformation(final int marketId) {
        final SuperMarket market = model.getMarket(marketId);
        final StoreAPIInterface storeServices = ServiceGenerator.createService(
                StoreAPIInterface.class, Constant.BASE_URL);
        storeServices.getFeaturedProducts(Integer.toString(marketId),
                true, 100,"publish").enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                market.setFeaturedProducts(response.body());
                //Get Offers
                OffersGrabber offersGrabber = new OffersGrabber(market);
                offersGrabber.execute();
                Log.e("ShowDataLog", "OffersGrabber");
            }

            @Override
            public void onFailure(@NonNull Call<List<Product>> call, Throwable t) {
                Log.e("ShowDataLog", "Error Happened While Retrieve Information");
            }
        });
    }

    public void requestMarketCategories(int marketId) {
        if (marketId == 0)
            return;
        final SuperMarket market = model.getMarket(marketId);
        final StoreAPIInterface storeServices = ServiceGenerator.createService(
                StoreAPIInterface.class, Constant.BASE_URL_V2);
        storeServices.getSuperMarketCategories(Integer.toString(marketId), Locale.getDefault().getLanguage())
                .enqueue(new Callback<CategoriesResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<CategoriesResponse> call,
                                           @NonNull Response<CategoriesResponse> response) {

                        CategoriesResponse res = response.body();
                        Log.e("resresres",new Gson().toJson(res));
                        if (res==null || res.getData() == null ){
                            market.setCategories(new ArrayList<>());
                            notifyMarketCategoriesObservers();
                            return;
                        }
                        List<Category> categories = removeEmptyCategory(res.getData());
                        market.setCategories(categories);
                        notifyMarketCategoriesObservers();
                    }

                    @Override
                    public void onFailure(@NonNull Call<CategoriesResponse> call, Throwable t) {
                        //TODO Handle Error
                    }
                });
    }

    public AreaResponse getSupportedAreas() {
        return model.getAreas();
    }

    public void requestProductList(Integer catId, int marketId, int page) {
        final StoreAPIInterface storeServices = ServiceGenerator.createService(
                StoreAPIInterface.class, Constant.BASE_URL);
        storeServices.getProducts(catId.toString(), Integer.toString(marketId), page,"publish")
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {


                        addListOfProducts(response.body());
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Product>> call, Throwable t) {
                        //TODO Handle Error
                    }
                });
    }

    public void searchProducts(int marketId, String query) {
        StoreAPIInterface storeServices = ServiceGenerator.createService(
                StoreAPIInterface.class, Constant.BASE_URL_V2);
        storeServices.searchProducts(marketId, query).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(@NonNull Call<SearchResponse> call, @NonNull Response<SearchResponse> response) {
                SearchResponse productResponse = response.body();
                assert productResponse != null;
                if (productResponse.getProductList() == null) {
                    notifyProductListObservers(new ArrayList<Product>());
                }
                else {
                    notifyProductListObservers(productResponse.getProductList());
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchResponse> call, Throwable t) {
                notifyProductListObservers(null);
            }
        });
    }
    public void searchProductsOnAllApp(String text) {
        StoreAPIInterface storeServices = ServiceGenerator.createService(
                StoreAPIInterface.class, Constant.BASE_URL_V2);
        storeServices.SearchAllProducts(text).enqueue(new Callback<AllOffersResponse>() {
            public void onResponse(@NonNull Call<AllOffersResponse> call, @NonNull Response<AllOffersResponse> response) {
                if (response.body().getDataproduct() == null) {
                    notifyProductListObservers(new ArrayList<>());
                }
                else {
                    ArrayList<Product> listPro = new ArrayList<>();
                    for (int i = 0 ; i<response.body().getDataproduct().size();i++){
                        Product item = response.body().getDataproduct().get(i).getProduct();
                        item.setImages(response.body().getDataproduct().get(i).getImages());
                        item.setStore(response.body().getDataproduct().get(i).getStore());
                        listPro.add(item);
                    }
                    notifyProductListObservers(listPro);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AllOffersResponse> call, Throwable t) {
                notifyProductListObservers(null);
            }
        });
    }
    public void GetAllOffersOfApp() {
        StoreAPIInterface storeServices = ServiceGenerator.createService(
                StoreAPIInterface.class, Constant.BASE_URL_V2);
        storeServices.GetAllOffers().enqueue(new Callback<AllOffersResponse>() {
            @Override
            public void onResponse(@NonNull Call<AllOffersResponse> call, @NonNull Response<AllOffersResponse> response) {
                Log.e("responseall","response");
                if (response.body().getDataproduct() == null) {
                    notifyProductListObservers(new ArrayList<Product>());
                }
                else {
                    ArrayList<Product> listPro = new ArrayList<>();
                    for (int i = 0 ; i<response.body().getDataproduct().size();i++){
                        Product item = response.body().getDataproduct().get(i).getProduct();
                        item.setImages(response.body().getDataproduct().get(i).getImages());
                        item.setStore(response.body().getDataproduct().get(i).getStore());
                        listPro.add(item);
                    }
                    notifyProductListObservers(listPro);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AllOffersResponse> call, Throwable t) {
                notifyProductListObservers(null);
                Log.e("responseall2",t.getMessage());
            }
        });
    }
    public void GetAllProductsOfAppById(int id) {
        StoreAPIInterface storeServices = ServiceGenerator.createService(
                StoreAPIInterface.class, Constant.BASE_URL_V2);
        storeServices.getAllProducts(String.valueOf(id), 50,"publish").enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                if (response.body() == null)
                    notifyProductListObservers(new ArrayList<>());
                else
                    notifyProductListObservers(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<List<Product>> call, Throwable t) {
                notifyProductListObservers(null);
                Log.e("responseall2",t.getMessage());
            }
        });
    }
    public void GetAllProductsOfApp() {
        StoreAPIInterface storeServices = ServiceGenerator.createService(
                StoreAPIInterface.class, Constant.BASE_URL_V2);
        storeServices.getAllProducts( 50,"publish").enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                if (response.body() == null)
                    notifyProductListObservers(new ArrayList<>());
                else
                    notifyProductListObservers(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<List<Product>> call, Throwable t) {
                notifyProductListObservers(null);
                Log.e("responseall2",t.getMessage());
            }
        });
    }

    public void requestUserData(String userToken) {
        Log.e("currentUser","requestUserData");
        UserDataGrabber grabber = new UserDataGrabber(userToken);
        grabber.execute();
    }

    public void updateUserData(String Name, String phone,String marketname,String store_address,String marketphone,String marketcategory) {
        UserUpdateRequest updater = new UserUpdateRequest();
        updater.setFirstName(Name.split(" ")[0]);
        updater.setLastName(Name.replace(updater.getFirstName()+" ",""));

        Billing billingAdd = new Billing();
        billingAdd.setPhone(phone);
        billingAdd.setAddress1(ModelController.getInstance().getModel().getUser().getBilling().getAddress1());
        billingAdd.setAddress2(ModelController.getInstance().getModel().getUser().getBilling().getAddress2());
        updater.setBilling(billingAdd);

        MetaDatum metaDatum = new MetaDatum();
        metaDatum.setKey("store_name");
        metaDatum.setValue(marketname);
        updater.getMetaData().add(metaDatum);

        MetaDatum metaDatum2 = new MetaDatum();
        metaDatum2.setKey("store_catogry");
        metaDatum2.setValue(marketcategory);
        updater.getMetaData().add(metaDatum2);

        MetaDatum metaDatum3 = new MetaDatum();
        metaDatum3.setKey("store_address");
        metaDatum3.setValue(store_address);
        updater.getMetaData().add(metaDatum3);

        MetaDatum metaDatum4 = new MetaDatum();
        metaDatum4.setKey("store_phone");
        metaDatum4.setValue(marketphone);
        updater.getMetaData().add(metaDatum4);



        UserUpdateSubmitter submitter = new UserUpdateSubmitter(updater,
                model.getUser().getId().toString(), model.getWooCommerceUserToken());
        submitter.execute();
    }

    public void submitOrder(Context context, String contactAddress, String contactPhone, String deliveryTime , String shipping , String customer_note) {
        AppDatabase db = Room.databaseBuilder(context,
                AppDatabase.class, "CartDB").allowMainThreadQueries().build();

        OrderSubmitter submitter = new OrderSubmitter(context,contactAddress, contactPhone, db.TransactionsDao().getAll(),
                deliveryTime, model.getUser(), model.getWooCommerceUserToken() , shipping , customer_note);
        submitter.execute();
    }

    public void requestOrderList(int page) {
        OrdersGrabber grabber = new OrdersGrabber(model.getWooCommerceUserToken(),page);
        grabber.execute();
    }

    public void orderSubmitStatus(boolean status) {
        notifyOrderSubmitObserver(status);
    }

    public void addListOfProducts(List<Product> products) {
        if (products != null) {
            model.setProductList(products);
            notifyProductListObservers(products);
        }
    }

    public void marketInfoReady(SuperMarket market) {
        if (market != null) {
            notifyMarketDataObservers();
        }
    }

    public void setUserInfo(String token, UserData user) {
        Log.e("currentUser","setUserInfo");
        if (user != null && token != null) {
            model.setUser(user);
            model.setWooCommerceUserToken(token);
        }
        notifyUserDataObserver();
    }

    public void updateUserInfo(UserData user) {
        boolean result = true;
//        if (user != null) {
//            model.setUser(user);
//            result = true;
//        }

        notifyUserDataUpdateRequester(result);
    }

    public void setOrdersList(List<OrderResponse> orders) {
        notifyOrdersListObserver(orders);
    }

    public void setMarketCategories(String marketId, List<Category> categories) {

    }

    public void attachToMarketListObservers(IMarketsListObserver observer) {
        if (observer != null && marketListObservers != null) {
            marketListObservers.add(observer);
        }
    }

    public void detachFromMarketListObservers(IMarketsListObserver observer) {
        if (observer != null && marketListObservers != null) {
            if (marketListObservers.contains(observer)) {
                marketListObservers.remove(observer);
            }
        }
    }

    public void attachToMarketDataObservers(IMarketDataObserver observer) {
        if (observer != null && marketDataObservers != null) {
            marketDataObservers.add(observer);
        }
    }

    public void detachFromMarketDataObservers(IMarketDataObserver observer) {
        if (observer != null && marketListObservers != null) {
            if (marketDataObservers.contains(observer)) {
                marketDataObservers.remove(observer);
            }
        }
    }

    public void attachToMarketCategoriesObservers(IMarketCategoriesObserver observer) {
        if (observer != null && marketCategoriesObservers != null) {
            marketCategoriesObservers.add(observer);
        }
    }

    public void detachFromMarketCategoriesObservers(IMarketCategoriesObserver observer) {
        if (observer != null && marketCategoriesObservers != null) {
            if (marketCategoriesObservers.contains(observer)) {
                marketCategoriesObservers.remove(observer);
            }
        }
    }


    public void attachToAreaListObservers(IAreaListObserver observer){
        if(observer!= null && areaListObservers != null){
            areaListObservers.add(observer);
        }
    }

    public void detachFromAreaListObservers(IAreaListObserver observer){
        if(observer!= null && areaListObservers != null){
            if(areaListObservers.contains(observer)) {
                areaListObservers.remove(observer);
            }
        }
    }

    public void attachToProductListObservers(IProductListObserver observer) {
        if (observer != null && productListObservers != null) {
            productListObservers.add(observer);
        }
    }

    public void detachFromProductListObservers(IProductListObserver observer) {
        if (observer != null && productListObservers != null) {
            if (productListObservers.contains(observer)) {
                productListObservers.remove(observer);
            }
        }
    }

    public void attachToUserDataObservers(IUserDataObserver observer) {
        if (observer != null && userDataObservers != null) {
            userDataObservers.add(observer);
        }
    }

    public void detachFromUserDataObservers(IUserDataObserver observer) {
        if (observer != null && userDataObservers != null) {
            if (userDataObservers.contains(observer)) {
                userDataObservers.remove(observer);
            }
        }
    }

    public void attachToUserDataUpdateObservers(IUserDataUpdateObserver observer) {
        if (observer != null && userDataUpdateObservers != null) {
            userDataUpdateObservers.add(observer);
        }
    }

    public void detachFromDataUpdateObservers(IUserDataUpdateObserver observer) {
        if (observer != null && userDataUpdateObservers != null) {
            if (userDataUpdateObservers.contains(observer)) {
                userDataUpdateObservers.remove(observer);
            }
        }
    }

    public void attachToOrderSubmitObservers(IOrderSubmitObserver observer) {
        if (observer != null && orderSubmitObservers != null) {
            orderSubmitObservers.add(observer);
        }
    }

    public void detachFromOrderSubmitObservers(IOrderSubmitObserver observer) {
        if (observer != null && orderSubmitObservers != null) {
            if (orderSubmitObservers.contains(observer)) {
                orderSubmitObservers.remove(observer);
            }
        }
    }

    public void attachToOrderListObservers(IOrderListObserver observer) {
        if (observer != null && orderListObservers != null) {
            orderListObservers.add(observer);
        }
    }

    public void detachFromOrderListObservers(IOrderListObserver observer) {
        if (observer != null && orderListObservers != null) {
            if (orderListObservers.contains(observer)) {
                orderListObservers.remove(observer);
            }
        }
    }

    private void notifyMarketListObservers() {
        for (IMarketsListObserver observer : marketListObservers
                ) {
            observer.onMarketListChanged();
        }
    }

    public void notifyMarketListObservers(List<SuperMarket> superMarkets) {
        for (IMarketsListObserver observer : marketListObservers
                ) {
            observer.onMarketListReady(superMarkets);
        }
    }

    private void notifyMarketDataObservers() {
        for (IMarketDataObserver observer : marketDataObservers
                ) {
            observer.onMarketDataReady();
        }
    }

    private void notifyMarketCategoriesObservers() {
        for (IMarketCategoriesObserver observer : marketCategoriesObservers
                ) {
            observer.onCategoriesListChanged();
        }
    }


    public void notifyAreaListObservers(){
        for (IAreaListObserver observer:areaListObservers) {
            observer.onAreaListReady();
        }
    }

    private void notifyProductListObservers(List<Product> products) {
        for (IProductListObserver observer : productListObservers) {
            observer.onProductListReady(products);
        }
    }

    private void notifyUserDataObserver() {
        if (userDataObservers !=null){
            for (IUserDataObserver observer : userDataObservers) {
                observer.onUserDataReady();
            }
        }
    }

    private void notifyOrderSubmitObserver(boolean status) {
        for (IOrderSubmitObserver observer : orderSubmitObservers
                ) {
            observer.onOrderSubmitStatus(status);
        }
    }

    private void notifyOrdersListObserver(List<OrderResponse> orders) {
        for (IOrderListObserver observer : orderListObservers
                ) {
            observer.onOrderListReady(orders);
        }
    }

    private void notifyUserDataUpdateRequester(boolean result) {
        for (IUserDataUpdateObserver observer : userDataUpdateObservers
                ) {
            observer.onUserUpdateStatusChange(result);
        }
    }




    private List<Category> removeEmptyCategory(List<Category> categories) {
        List<Category> result = new ArrayList<>();
        for (Category category : categories) {
            if (category.getNoOfProducts() > 0) {
                result.add(category);
            }
        }
        return result;
    }

}
