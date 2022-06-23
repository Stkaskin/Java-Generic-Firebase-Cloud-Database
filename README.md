# Java-Generic-Firebase-Cloud-Database
Generic CRUD Operations 
Generic Firebase CRUD and Search With OOP

Connection Firebase 

![image](https://user-images.githubusercontent.com/90522945/175180725-fedc2418-ab37-4231-91bf-47a6f46f2d33.png)

![image](https://user-images.githubusercontent.com/90522945/175180891-86545f0b-5820-4174-8656-a31a7fd24aa3.png)

![image](https://user-images.githubusercontent.com/90522945/175181098-4756b91c-3b72-457b-8af3-20286a068faf.png)



Import 

![image](https://user-images.githubusercontent.com/90522945/175179335-4c9df011-91ee-4066-9e54-105860994cb3.png)



Sample Class : Table 

    public class Table implements IFirebase {
        
        String id;
      int displayRank;
      String name;
      int status;
      public String getId() {
          return id;
      }
    public void setId(String id) {
        this.id = id;
    }

    public int getDisplayRank() {
        return displayRank;
    }

    public void setDisplayRank(int displayRank) {
        this.displayRank = displayRank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String TableName() {
        return "Table";
    }




Add 

        Table table = new Table();
        table.setStatus(1);
        table.setName("New Table -222");
        String id = FirebaseService.Add(table);


Get then Update 
    
        Table upTable = FirebaseService.Get(Table.class, id);
        upTable.setName("Old Table -222");
        upTable.setStatus(0);
        FirebaseService.UpdateData(upTable);
        
        
Get Then Delete(Remove)

        Table delTable = FirebaseService.Get(Table.class, id);
        FirebaseService.Delete(delTable);
        
Get All Data

  
        ArrayList<Table> tables = FirebaseService.Get(Table.class);
        
Get Data from id
    
    Table getTable= FirebaseService.Get(Table.class, "documentid");
    
Get Where Data 

      //import com.google.firebase.firestore.Query;
        Query query=FirebaseService.QueryCreate(Table.class).
                whereEqualTo("fieldname","yourdata");
        FirebaseService.Get(Table.class, query);


Get Where Data Sample-2 
    
    //import com.google.firebase.firestore.Query;
        Query query=FirebaseService.QueryCreate(Table.class).
                whereEqualTo("email","xx@mail.com").
                whereEqualTo("password","123456");
        FirebaseService.Get(Table.class, query);





        
        

