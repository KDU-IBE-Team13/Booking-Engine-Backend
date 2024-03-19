package com.example.ibeproject.constants;

public class GraphQLConstants {

    private GraphQLConstants() {

    }
  
    public static final String LIST_NIGHTLY_RATES_QUERY_STRING = 
        "{ " +
        "  getProperty( where: {property_id: 13} ) { " +
        "    property_id " +
        "    room_type { " +
        "      room_type_id " +
        "      room_type_name " +
        "      room_rates { " +
        "        room_rate { " +
        "          basic_nightly_rate " +
        "          date " +
        "        } " +
        "      } " +
        "    } " +
        "  } " +
        "}";
        
    public static final String LIST_PROPERTIES_QUERY_STRING = 
        "{ " +
        "  listProperties { " +
        "    property_id " +
        "    property_name " +
        "  } " +
        "}";
}
