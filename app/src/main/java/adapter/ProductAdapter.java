package adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.qrcodeapp.R;
import com.squareup.picasso.Picasso;

import model.Product;

public class ProductAdapter extends ArrayAdapter<Product> {
    Activity context;
    int resource;
    public ProductAdapter(Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View custom = context.getLayoutInflater().inflate(resource, null);
//        TextView txtId = custom.findViewById(R.id.txtId);
        ImageView img = custom.findViewById(R.id.imgPic);
        TextView txtName = custom.findViewById(R.id.txtName);
        TextView txtPrice = custom.findViewById(R.id.txtPrice);
        TextView txtQuantity = custom.findViewById(R.id.txtQuantity);
//        TextView txtDescription = custom.findViewById(R.id.txtDescription);
        Product product = getItem(position);
        Picasso.get().load(product.getPicture()).into(img);
//        txtId.setText(product.getProductId());
        txtName.setText(product.getName());
        txtPrice.setText(product.getPrice()+" vnđ");
        txtQuantity.setText("Số lượng: "+product.getQuantity());
//        txtDescription.setText(product.getDescription());
        return custom;
    }
}
