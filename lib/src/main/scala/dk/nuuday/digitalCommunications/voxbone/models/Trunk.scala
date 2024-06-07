package dk.nuuday.digitalCommunications.voxbone.models

/*
{
    "trunks": [
        {
            "trunkId": 165,
            "zone": "A",
            "capacity": 290,
            "description": "default trunk",
            "countDidsOnTrunk": 10484
        },
        {
            "trunkId": 3324,
            "zone": "WORLDWIDE",
            "capacity": 140,
            "description": "Default trunk",
            "countDidsOnTrunk": 890
        }
    ]
}
 */

case class Trunk (trunkId: Int, zone: Zone, capacity: Int,description: Description, coundDidsOnTrunk: Int)
case class Trunks(trunks: java.util.Set[Trunk])
