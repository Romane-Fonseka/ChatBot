package com.example.agrogeo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Signin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        TextView name =(TextView) findViewById(R.id.Wname);
        TextView mobile =(TextView) findViewById(R.id.Wno);
        TextView password =(TextView) findViewById(R.id.psd);
        TextView repsd =(TextView) findViewById(R.id.repsd);
        Button signinBtn = (Button) findViewById(R.id.SigninBtn);

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name_w = name.getText().toString();
                String mobile_w = mobile.getText().toString();
                String password_w = password.getText().toString();
                String repsd_w = repsd.getText().toString();
                
                if (password_w.equals(repsd_w)); //&& (password.equals("")));
                {
                    Toast.makeText(Signin.this, "Welcome", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(Signin.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }

            }
        });
        
        
                public Signin(String name, String mobile, String password, String repsd) {
            this.name_w = name;
            this.mobile_w = mobile;
            this.password_w = password;
            this.repsd_w = repsd;
        }

        public String getname() {
            return name;
        }

        public void setId(String name) {
            this.name = name;
        }

        public String getmobile() {
            return mobile;
        }

        public void setpassword(String password) {
            this.password_w = password;
        }

        public String getrepsd() {
            return repsd;
        }

        public void setrepsd(String repsd) {
            this.repsd = repsd;
        }
        

        @Override
        public String toString() {
            return "Signin{" +
                    "name=" + name +
                    ", mobile='" + mobile + '\'' +
                    ", password='" + password + '\'' +
                    ", repsd=" + repsd +
                    '}';
            
            private static void insertData(Signin signin, Connection connection) throws SQLException {
    log.info("Insert data");
    PreparedStatement insertStatement = connection
            .prepareStatement("INSERT INTO Signin (name, mobile, password, repsd) VALUES (?, ?, ?, ?);");

    insertStatement.setLong(1, signin.getname());
    insertStatement.setString(2, signin.getmobile());
    insertStatement.setString(3, signin.getpassword());
    insertStatement.setBoolean(4, signin.getrepsd());
    insertStatement.executeUpdate();
                
                
}
        }
    }
    
    
    private static Signin readData(Connection connection) throws SQLException {
    log.info("Read data");
    PreparedStatement readStatement = connection.prepareStatement("SELECT * FROM todo;");
    ResultSet resultSet = readStatement.executeQuery();
    if (!resultSet.next()) {
        log.info("There is no data in the database!");
        return null;
    }
    Signin signin = new Signin();
    signin.setname(resultSet.getString("name"));
    signin.setmobile(resultSet.getString("mobile"));
    signin.setpassword(resultSet.getString("password"));
    signin.setrepsd(resultSet.getString("repsd"));
    log.info("Data read from the database: " + signin.toString());
    return signin;
}

    
    private static void updateData(Signin signin, Connection connection) throws SQLException {
    log.info("Update data");
    PreparedStatement updateStatement = connection
            .prepareStatement("UPDATE signin SET name = ?, password = ?, repsd = ? WHERE mobile = ?;");

    updateStatement.setString(1, signin.getname());
    updateStatement.setString(2, signin.getpassword());
    updateStatement.setString(3, signin.getrepsd());
    updateStatement.setString(4, signin.getmobile());
    updateStatement.executeUpdate();
    readData(connection);
}
    
    private static void deleteData(Signin signin, Connection connection) throws SQLException {
    log.info("Delete data");
    PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM todo WHERE mobile = ?;");
    deleteStatement.setLong(1, signin.getmobile());
    deleteStatement.executeUpdate();
    readData(connection);
}
    }
}
