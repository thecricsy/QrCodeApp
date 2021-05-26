package adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.qrcodeapp.R;

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
        TextView txtId = custom.findViewById(R.id.txtId);
        TextView txtName = custom.findViewById(R.id.txtName);
        TextView txtPrice = custom.findViewById(R.id.txtPrice);
        TextView txtDescription = custom.findViewById(R.id.txtDescription);
        Product product = getItem(position);
        txtId.setText(product.getProductId());
        txtName.setText(product.getName());
        txtPrice.setText(product.getPrice());
        txtDescription.setText(product.getDescription());
        return custom;
    }
}
