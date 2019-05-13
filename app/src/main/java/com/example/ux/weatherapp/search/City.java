package com.example.ux.weatherapp.search;

/**
 * pojo to hold cities
 */

public class City {
    /*
    {
    "id": 2327124,
    "name": "Oko",
    "country": "NG",
    "coord": {
      "lon": 5.59383,
      "lat": 6.2995
    }
  }
     */
    private long id;
    private String name;
    private String country;
    private Coordinate coord;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Coordinate getCoord() {
        return coord;
    }

    public void setCoord(Coordinate coord) {
        this.coord = coord;
    }

    public class Coordinate {
        private double lon;
        private double lat;

        Coordinate(double lon, double lat) {
            this.lon = lon;
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }
    }
}
