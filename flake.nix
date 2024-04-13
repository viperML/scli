{
  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";

    sbt = {
      url = "github:zaninime/sbt-derivation/master";
      inputs.nixpkgs.follows = "nixpkgs";
    };
  };

  outputs =
    {
      self,
      nixpkgs,
      sbt,
    }:
    let
      system = "x86_64-linux";
      pkgs = nixpkgs.legacyPackages.${system};
    in
    {
      packages.${system}.default =
        (sbt.mkSbtDerivation.${system}).withOverrides ({ stdenv = pkgs.llvmPackages.stdenv; })
          {
            pname = "scli";
            version = "0.1.0";
            src = ./.;
            depsSha256 = "sha256-EKaMR22kA1nSNu4V4wl4fkEMnyxFm4M7dm946YjaHDA=";
            env.NIX_CFLAGS_COMPILE = "-Wno-unused-command-line-argument";
            hardeningDisable = [ "fortify" ];
          };
    };
}
